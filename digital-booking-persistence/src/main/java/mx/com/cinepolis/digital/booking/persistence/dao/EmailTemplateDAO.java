/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package mx.com.cinepolis.digital.booking.persistence.dao;

import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.EmailTemplateDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.EmailTemplateDO}
 * 
 * @author afuentes
 */
public interface EmailTemplateDAO extends GenericDAO<EmailTemplateDO>
{

  /**
   * Method that finds the email template for the specified region and email type.
   * 
   * @param weekTO {@link WeekTO} with the week identifier.
   * @param regionId Region identifier.
   * @return emailTypeId Email type identifier.
   * @author afuentes
   */
  EmailTemplateDO findByRegionAndEmailType( Long regionId, int emailTypeId );

}
