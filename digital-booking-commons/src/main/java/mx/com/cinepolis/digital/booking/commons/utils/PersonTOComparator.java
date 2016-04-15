package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.PersonTO;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Comparator class between two instances of {@link mx.com.cinepolis.digital.booking.commons.to.PersonTO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class PersonTOComparator implements Comparator<PersonTO>, Serializable
{
  private static final long serialVersionUID = -4235507178975871353L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( PersonTO person1, PersonTO person2 )
  {
    CompareToBuilder comp = new CompareToBuilder();
    comp.append( person1.getName(), person2.getName() );
    comp.append( person1.getDsLastname(), person2.getDsLastname() );
    comp.append( person1.getDsMotherLastname(), person2.getDsMotherLastname() );
    return comp.toComparison();
  }
}
