/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.model.ImmutableValueModel;

public abstract class AbstractSimplePager<
	RP extends Serializable, 
	P extends PagerModel<RP, P, SimplePagerModel.Command>
>
	extends AbstractPagerModel<RP, P,  SimplePagerModel.Command> 
	implements SimplePagerModel<RP, P> {

	
	/* 
	 * Returns the offset of the first the of the current page.
	 */
	public abstract ImmutableValueModel<Long> currentPageOffset();
	
	/*
	 * Returns the offset of the first the of the current page.
	 */
	public abstract ImmutableValueModel<Long> available();	

}
