package com.appspot.relaxe.paging;

import com.appspot.relaxe.ui.action.Action;

public interface PagerModel 	{
	
	Action getFirstPageAction();

	Action getPreviousPageAction();

	Action getNextAction();

	Action getPreviousAction();

	Action getNextPageAction();
	
	Action getLastPageAction();
	
	Action getRefreshAction();

}
