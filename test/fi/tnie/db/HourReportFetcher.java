/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.Connection;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.HourReport.Factory;
import fi.tnie.db.gen.ent.personal.HourReport.Holder;
import fi.tnie.db.gen.ent.personal.HourReport.MetaData;
import fi.tnie.db.gen.ent.personal.HourReport.QueryTemplate;
import fi.tnie.db.gen.ent.personal.HourReport.Type;

public class HourReportFetcher
	extends SynchronousFetcher<
	HourReport.Attribute, 
	HourReport.Reference, 
	HourReport.Type, 
	HourReport, 
	HourReport.Holder, 
	HourReport.Factory, 
	HourReport.MetaData, 
	HourReport.QueryTemplate>
{

	public HourReportFetcher(Implementation imp, Connection c) {			
		super(new EntityQueryExecutor<HourReport.Attribute, fi.tnie.db.gen.ent.personal.HourReport.Reference, Type, HourReport, Holder, Factory, MetaData, QueryTemplate>(imp), c);		
	}
}