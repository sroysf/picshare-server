package com.codechronicle.picshare.server.dao;

import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.entity.Tag;

/**
 * This DAO exists because of all the restrictions around using @Transactional
 * In proxy mode, it only works on an annotated class using dynamic proxies. Thus, we must
 * have an interface that abstracts out the behavior that actually requires a transactional
 * commit.
 * 
 * Adding this basic DAO, just for the purposes of managing transactions, ends up being the
 * cleanest solution.
 * 
 * @author sroy
 *
 */
public interface TransactionalDAO {
	
	Image saveOrUpdateImage(Image image); 
	
	Tag saveOrUpdateTag(Tag tag);
}
