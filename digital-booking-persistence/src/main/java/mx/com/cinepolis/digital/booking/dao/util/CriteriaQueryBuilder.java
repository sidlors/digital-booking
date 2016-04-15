package mx.com.cinepolis.digital.booking.dao.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;

import org.apache.commons.lang.time.DateUtils;

/**
 * Utility class that creats predicates of {@link javax.persistence.criteria.Predicate}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public final class CriteriaQueryBuilder
{
  private CriteriaQueryBuilder()
  {
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param root
   * @param criteriaBuilder
   * @param filter
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> Predicate applyFilterRootEqual( Map<ModelQuery, Object> filters, ModelQuery modelQuery, Root root,
      CriteriaBuilder criteriaBuilder, Predicate filter )
  {
    Predicate filterPredicate = filter;
    if( filters.containsKey( modelQuery ) && root != null )
    {
      Path<T> pathFilter = root.get( modelQuery.getQuery() );
      filterPredicate = criteriaBuilder.and( filter, criteriaBuilder.equal( pathFilter, filters.get( modelQuery ) ) );
    }
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param root
   * @param criteriaBuilder
   * @param filter
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static Predicate applyFilterRootEqualDate( Map<ModelQuery, Object> filters, ModelQuery modelQuery, Root root,
      CriteriaBuilder criteriaBuilder, Predicate filter )
  {
    Predicate filterPredicate = filter;
    if( filters.containsKey( modelQuery ) && filters.get( modelQuery ) instanceof Date && root != null )
    {
      Date date = (Date) filters.get( modelQuery );
      Path<Date> pathFilter = root.get( modelQuery.getQuery() );
      Date initDate = DateUtils.truncate( date, Calendar.DATE );
      Date endDate = DateUtils.truncate( date, Calendar.DATE );
      endDate = DateUtils.setHours( endDate, 23 );
      endDate = DateUtils.setMinutes( endDate, 59 );
      endDate = DateUtils.setSeconds( endDate, 59 );
      endDate = DateUtils.setMilliseconds( endDate, 999 );
      filterPredicate = criteriaBuilder.and( filter, criteriaBuilder.between( pathFilter, initDate, endDate ) );
    }
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param root
   * @param criteriaBuilder
   * @param filter
   * @return
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static Predicate applyFilterRootLike( Map<ModelQuery, Object> filters, ModelQuery modelQuery, Root root,
      CriteriaBuilder criteriaBuilder, Predicate filter )
  {
    Predicate filterPredicate = filter;
    if( filters.containsKey( modelQuery ) && root != null )
    {
      Path<String> pathFilter = root.get( modelQuery.getQuery() );
      Expression<String> lower = criteriaBuilder.lower( pathFilter );
      filterPredicate = criteriaBuilder.and( filter,
        criteriaBuilder.like( lower, "%" + filters.get( modelQuery ).toString().toLowerCase() + "%" ) );

    }
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param join
   * @param criteriaBuilder
   * @param filter
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> Predicate applyFilterJoinEqual( Map<ModelQuery, Object> filters, ModelQuery modelQuery, Join join,
      CriteriaBuilder criteriaBuilder, Predicate filter )
  {
    Predicate filterPredicate = filter;
    if( filters.containsKey( modelQuery ) && join != null )
    {
      Path<T> pathFilter = join.get( modelQuery.getQuery() );
      filterPredicate = criteriaBuilder.and( filter, criteriaBuilder.equal( pathFilter, filters.get( modelQuery ) ) );
    }
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param join
   * @param criteriaBuilder
   * @param filter
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static Predicate applyFilterJoinLike( Map<ModelQuery, Object> filters, ModelQuery modelQuery, Join join,
      CriteriaBuilder criteriaBuilder, Predicate filter )
  {
    Predicate filterPredicate = filter;
    if( filters.containsKey( modelQuery ) && join != null )
    {
      Path<String> pathFilter = join.get( modelQuery.getQuery() );
      Expression<String> lower = criteriaBuilder.lower( pathFilter );
      String filterString = filters.get( modelQuery ).toString().toLowerCase();
      filterString = filterString.replaceAll( " ", "%" );
      filterPredicate = criteriaBuilder.and( filter, criteriaBuilder.like( lower, "%" + filterString + "%" ) );
    }
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param join
   * @param criteriaBuilder
   * @param filter
   * @return
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static Predicate applyFilterJoinEqualDate( Map<ModelQuery, Object> filters, ModelQuery modelQuery, Join join,
      CriteriaBuilder criteriaBuilder, Predicate filter )
  {
    Predicate filterPredicate = filter;
    if( filters.containsKey( modelQuery ) && filters.get( modelQuery ) instanceof Date && join != null )
    {
      Date date = (Date) filters.get( modelQuery );
      Path<Date> pathFilter = join.get( modelQuery.getQuery() );
      Date initDate = DateUtils.truncate( date, Calendar.DATE );
      Date endDate = DateUtils.truncate( date, Calendar.DATE );
      endDate = DateUtils.setHours( endDate, 23 );
      endDate = DateUtils.setMinutes( endDate, 59 );
      endDate = DateUtils.setSeconds( endDate, 59 );
      endDate = DateUtils.setMilliseconds( endDate, 999 );
      filterPredicate = criteriaBuilder.and( filter, criteriaBuilder.between( pathFilter, initDate, endDate ) );
    }
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param join
   * @param criteriaBuilder
   * @param filter
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> Predicate applyFilterJoinEqualOr( Object[] values, ModelQuery modelQuery, Join join,
      CriteriaBuilder criteriaBuilder, CriteriaBuilder cbAux, Predicate filter )
  {
    Predicate filterPredicate = filter;
    Predicate filterCondition = cbAux.conjunction();

    boolean first = true;
    for( Object value : values )
    {
      if( value != null && join != null )
      {
        Path<T> pathFilter = join.get( modelQuery.getQuery() );
        if( first )
        {
          filterCondition = cbAux.and( filterCondition, cbAux.equal( pathFilter, value ) );
          first = false;
        }
        else
        {
          filterCondition = cbAux.or( filterCondition, cbAux.equal( pathFilter, value ) );
        }
      }
    }
    filterPredicate = criteriaBuilder.and( filterPredicate, filterCondition );
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param join
   * @param secondJoin
   * @param criteriaBuilder
   * @param cbAux
   * @param filter
   * @return filterPredicate
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> Predicate applyFilterJoinEqualOrSecondJoinEqual( Map<ModelQuery, Object> filters,
      ModelQuery modelQuery, Join join, Join secondJoin, CriteriaBuilder criteriaBuilder, CriteriaBuilder cbAux,
      Predicate filter )
  {
    Object value = null;
    if( filters.containsKey( modelQuery ) )
    {
      value = filters.get( modelQuery );
    }
    Predicate filterPredicate = filter;
    Predicate filterCondition = cbAux.conjunction();

    Path<T> pathFilter = join.get( modelQuery.getQuery() );
    if( value != null && join != null )
    {
      filterCondition = cbAux.and( filterCondition, cbAux.equal( pathFilter, value ) );
    }
    if( value != null && secondJoin != null )
    {
      Path<T> secondPathFilter = secondJoin.get( modelQuery.getQuery() );
      filterCondition = cbAux.or( filterCondition, cbAux.equal( secondPathFilter, value ) );
    }
    filterPredicate = criteriaBuilder.and( filterPredicate, filterCondition );
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param join
   * @param secondJoin
   * @param criteriaBuilder
   * @param cbAux
   * @param filter
   * @return filterPredicate
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> Predicate applyFilterJoinEqualOrSecondJoinEqualForDataSet(Object[] dataSet, Map<ModelQuery, Object> filters,
      ModelQuery modelQuery, Join join, Join secondJoin, CriteriaBuilder criteriaBuilder, CriteriaBuilder cbAux,
      Predicate filter )
  {
    Predicate filterPredicate = filter;
    Predicate filterCondition = cbAux.conjunction();
    boolean isFirst = Boolean.TRUE;
    Path<T> pathFilter = join.get( modelQuery.getQuery() );
    for( Object value : dataSet )
    {
      if( value != null && join != null )
      {
        if(isFirst)
        {
          filterCondition = cbAux.and( filterCondition, cbAux.equal( pathFilter, value ) );
          isFirst = Boolean.FALSE;
        }
        else
        {
          filterCondition = cbAux.or( filterCondition, cbAux.equal( pathFilter, value ) );
        }
      }
      if( value != null && secondJoin != null )
      {
        Path<T> secondPathFilter = secondJoin.get( modelQuery.getQuery() );
        filterCondition = cbAux.or( filterCondition, cbAux.equal( secondPathFilter, value ) );
      }
    }
    filterPredicate = criteriaBuilder.and( filterPredicate, filterCondition );
    return filterPredicate;
  }

  /**
   * Creates a criteria query
   * 
   * @param filters
   * @param modelQuery
   * @param join
   * @param criteriaBuilder
   * @param filter
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> Predicate applyFilterJoinNotNull( ModelQuery modelQuery, Join join,
      CriteriaBuilder criteriaBuilder, Predicate filter )
  {
    Path<T> pathFilter = join.get( modelQuery.getQuery() );
    Predicate filterPredicate = criteriaBuilder.and( filter, criteriaBuilder.isNotNull( pathFilter ) );

    return filterPredicate;
  }

}
