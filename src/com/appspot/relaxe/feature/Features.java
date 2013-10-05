/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.feature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.ddl.CreateSchema;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.DropSchema;
import com.appspot.relaxe.expr.ddl.DropTable;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Environment;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.query.QueryException;


public class Features
    extends AbstractFeature {
    
    private static final String FEATURES_SCHEMA = "features";    
    private static final String FEATURE_TABLE = "feature";
    private static final String DEPENDENCY_TABLE = "dependency";
    
    private List<Feature> featureList;

    private static Logger logger = Logger.getLogger(Features.class);
        
    public Features() {
        super(Features.class, 0, 1);        
    }

    @Override
    public SQLGenerator getSQLGenerator() {        
        return new Generator();
    }    
    
    public void addFeature(Feature feature) {
        if (feature == null) {
            throw new NullPointerException("'feature' must not be null");
        }
        
        getFeatureList().add(feature);
    }

    private List<Feature> getFeatureList() {
        if (featureList == null) {
            featureList = new ArrayList<Feature>();            
        }
    
        return featureList;
    }

    class Generator 
        implements SQLGenerator {

        @Override
        public SQLGenerationResult modify(Catalog cat)
                throws SQLGenerationException {
            
            final SQLGenerationResult result = new SQLGenerationResult();
            final Environment env = cat.getEnvironment();
            final Identifier sn = schemaName(env);            
            final Schema schema = cat.schemas().get(sn);            
                        
            if (schema == null) {
                result.add(new CreateSchema(sn));
            }
             
            Identifier features = featuresTable(env);
            
            { 
                BaseTable table = getBaseTable(schema, features);
                
                if (table == null) {
                    CreateTable ct = createFeaturesTable(cat, sn, features);
                    result.add(ct);
                }
            }
            
            {
                Identifier n = dependenciesTable(env);            
                BaseTable table = getBaseTable(schema, n);
            
                if (table == null) {
                    CreateTable ct = createDependenciesTable(cat, sn, n, features);
                    result.add(ct);
                }
            }
            
            return result;
        }
        
        
        private BaseTable getBaseTable(Schema schema, Identifier n) {
            return (schema == null) ? null : schema.baseTables().get(n);            
        }

        private CreateTable createFeaturesTable(Catalog cat, Identifier sn, Identifier tn) {
            // using catalog name as a qualifier would be too stiff, right?                
            SchemaElementName sen = new SchemaElementName(null, sn, tn);   
                        
         // TODO : replace TableDesign by CreateTable.Builder
            TableDesign td = new TableDesign(cat.getEnvironment(), sen);  
            // new TableDesign(cat.getEnvironment(), new CreateTable(sen));  
            
//              Maybe we should add a flag to column definition to mark 
//              autoincrement column and inspect that later?
            td.serial("ID");           
            td.varchar("NAME", 12);
            td.integer("VERSION_MAJOR");
            td.integer("VERSION_MINOR");
                        
            return td.newCreateTable();
        }
        
        private CreateTable createDependenciesTable(Catalog cat, Identifier sn, Identifier tn, Identifier features) {
            // using catalog name as a qualifier would be too stiff, right?                
            SchemaElementName sen = new SchemaElementName(null, sn, tn);
         // TODO : replace TableDesign by CreateTable.Builder
            TableDesign td = new TableDesign(cat.getEnvironment(), sen);  
                                    
            String dependent = "DEPENDENT";
            String dependency = "DEPENDENCY";
            
            td.integer(dependent); 
            td.integer(dependency);
            td.integer("MIN_MAJOR");
            td.integer("MAX_MAJOR");
            td.integer("MIN_MINOR");
            td.integer("MAX_MINOR");
            
            td.primaryKey();
                        
            return td.newCreateTable();
        }        

        @Override
        public SQLGenerationResult revert(Catalog cat)
                throws SQLGenerationException {
            
            Environment env = cat.getEnvironment();
            SQLGenerationResult r = new SQLGenerationResult();
            
            Schema schema = cat.schemas().get(schemaName(env));            
            drop(r, schema, dependenciesTable(env));
            drop(r, schema, featuresTable(env));
            drop(r, schema);
            
            return r;
        }    
                
        private void drop(SQLGenerationResult r, Schema schema) {
            if (schema != null) {
                r.add(new DropSchema(schema.getUnqualifiedName()));
            }
        }


        void drop(SQLGenerationResult result, Schema schema, Identifier name) {
            BaseTable table = getBaseTable(schema, name);
            
            if (table != null) {
                result.add(new DropTable(table.getName()));
            }                                            
        }        
        
        private Identifier schemaName(Environment env) {            
            return env.getIdentifierRules().toIdentifier(FEATURES_SCHEMA);        
        }
        
        private Identifier featuresTable(Environment env) {
            return env.getIdentifierRules().toIdentifier(FEATURE_TABLE);        
        }
        
        private Identifier dependenciesTable(Environment env) {
            return env.getIdentifierRules().toIdentifier(DEPENDENCY_TABLE);        
        }
    }
    
    public void installAll(Connection c, CatalogFactory cf, boolean intermediateCommit) 
        throws QueryException, SQLException, SQLGenerationException {
        
        logger().debug("installAll - enter");
        
        List<Feature> features = getFeatureList();
        
        {
            ArrayList<Feature> reversed = new ArrayList<Feature>(features);
            Collections.reverse(reversed);              
            boolean revert = true;
            
            for(int i = 0; i < 5; i++) {
                // revert all
                if (!process(c, cf, reversed, revert, intermediateCommit)) {                  
                }
            }
        }
                            
        for(int i = 0; i < 5; i++) {
            //  keep generating layers until any feature
            //  has nothing to add/remove:
            if (!process(c, cf, features, false, intermediateCommit)) {
                break;
            }
        }
        
        if (!intermediateCommit) {
            c.commit();    
        }
        
        logger().debug("installAll - exit");
    }

    private boolean process(Connection c, CatalogFactory cf, List<Feature> features, boolean revert, boolean commit) 
        throws QueryException, SQLException, SQLGenerationException {
    
        logger().debug("process - enter");
        
        Catalog cat = cf.create(c);
        int count = 0;
                  
        for (Feature f : features) {                            
            SQLGenerator g = f.getSQLGenerator();
              
            if (g != null) {
                SQLGenerationResult result = revert ? g.revert(cat) : g.modify(cat);
                List<Statement> list = result.statements();
                
                if (!list.isEmpty()) {
                  executeAll(c, list);
                  
                  if (commit) {
                      c.commit();
                  }
                  
                  count += list.size();                                    
    //              recreate catalog to get the updated meta-data:
                  cat = cf.create(c);
                }                
            }
        }
        
        logger().debug("process - exit: " + count);
    
        return count > 0;
    }

    private void executeAll(Connection c, List<Statement> list)
        throws SQLException {
        
        for (Statement statement : list) {
            String sql = statement.generate();
            
            logger().debug("query: " + sql);      
            PreparedStatement ps = c.prepareStatement(sql);
            
            try {
                ps.executeUpdate();      
            }
            finally {
                ps = QueryHelper.doClose(ps);                
            }
        }
    }
    
    public static Logger logger() {
        return Features.logger;
    }

}
