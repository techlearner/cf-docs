package com.springsource.html5expense.serviceImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.html5expense.model.Attachment;
import com.springsource.html5expense.service.AttachmentService;


@Service
public class JpaAttachmentServiceImpl implements AttachmentService{

	private EntityManager entityManager;

	public static final String EXPENSE_COLLECTION = "expense";

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	public Attachment getAttachment(Long attachmentId){

		//if(true){
			Attachment attachment = new Attachment();
			attachment.setId(attachmentId);
			attachment = entityManager.find(Attachment.class,attachmentId);
			return attachment;
		//}
		/*else{
			List<Attachment> attachmentList = mongoTemplate.find(new org.springframework.data.mongodb.core.query.Query(Criteria.where("id").is(attachmentId)), Attachment.class,EXPENSE_COLLECTION);
			return attachmentList!=null && attachmentList.size()>0?attachmentList.get(0):null;
		}*/
	}

	@Transactional
	public void save(Attachment attachment){
		if(true){
			entityManager.persist(attachment);
		}/*else{
			mongoTemplate.save(attachment);
		}*/

	}

	@Transactional
	public List<Attachment> getAttachmentByExpenseId(Long expenseId){
		Query query = entityManager.createQuery("select a from Attachment a where a.expense.id =:expenseId");
		query.setParameter("expenseId", expenseId);

		List<Attachment> attachmentList = query.getResultList();
		return attachmentList;
	}

	@Transactional
	public void deleteAttachment(Long id){
	//	if(true){
			Attachment attchment = getAttachment(new Long(id));
			getEntityManager().remove(attchment);
		/*}else{
			Attachment attachment = getAttachment(id);
			mongoTemplate.remove(attachment);
		}*/
	}

}
