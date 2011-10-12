/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.HourReport.Factory;
import fi.tnie.db.gen.ent.personal.HourReport.Holder;
import fi.tnie.db.gen.ent.personal.HourReport.MetaData;
import fi.tnie.db.gen.ent.personal.HourReport.QueryTemplate;
import fi.tnie.db.gen.ent.personal.HourReport.Type;
import fi.tnie.db.paging.DefaultEntityQueryPager;
import fi.tnie.db.paging.EntityFetcher;

public class HourReportPager
	extends DefaultEntityQueryPager<
	HourReport.Attribute, 
	HourReport.Reference, 
	HourReport.Type, 
	HourReport, 
	HourReport.Holder, 
	HourReport.Factory, 
	HourReport.MetaData, 
	HourReport.QueryTemplate>
{

	public HourReportPager(
			QueryTemplate template,
			EntityFetcher<fi.tnie.db.gen.ent.personal.HourReport.Attribute, fi.tnie.db.gen.ent.personal.HourReport.Reference, Type, HourReport, Holder, Factory, MetaData, QueryTemplate> fetcher) {
		super(template, fetcher, null, 20);
	}
	
}