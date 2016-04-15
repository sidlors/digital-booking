package mx.com.cinepolis.digital.booking.persistence.dao.impl.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTest_MSSQLUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingWeekDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingWeekScreenDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.CategoryDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.DistributorDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.EventDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.WeekDAOImpl;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class BookingsLoad extends AbstractDBEJBTest_MSSQLUnit
{

  private EventDAO eventDAO;
  private DistributorDAO distributorDAO;
  private TheaterDAO theaterDAO;
  private CategoryDAO categoryDAO;
  private BookingDAO bookingDAO;
  private BookingWeekDAO bookingWeekDAO;
  private BookingWeekScreenDAO bookingWeekScreenDAO;
  private WeekDAO weekDAO;

  public void setUp()
  {
    // instanciar el servicio
    eventDAO = new EventDAOImpl();
    distributorDAO = new DistributorDAOImpl();
    categoryDAO = new CategoryDAOImpl();
    theaterDAO = new TheaterDAOImpl();
    bookingDAO = new BookingDAOImpl();
    bookingWeekDAO = new BookingWeekDAOImpl();
    bookingWeekScreenDAO = new BookingWeekScreenDAOImpl();
    weekDAO = new WeekDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( eventDAO );
    connect( distributorDAO );
    connect( categoryDAO );
    connect( theaterDAO );
    connect( bookingDAO );
    connect( bookingWeekDAO );
    connect( bookingWeekScreenDAO );
    connect( weekDAO );
  }

  @Test
  @Ignore
  public void testData()
  {
    System.out.println( getData().length );
    int i = 0;
    for( int[] data : getData() )
    {

      if( data[1] == 264 )
      {
        System.out.println( "i = " + i );
        break;
      }
      i++;
    }
  }

  @Test
  @Ignore
  public void test() throws SQLException
  {
    Date ini = new Date();

    int[][] data = { { 7050, 193 }, { 7051, 194 }, { 7052, 158 }, { 7053, 180 }, { 7054, 183 }, { 7057, 156 },
        { 7058, 212 }, { 7059, 165 }, { 7060, 173 }, { 7061, 255 }, { 7062, 261 }, { 7063, 265 }, { 7064, 241 },
        { 7065, 177 }, { 7067, 171 }, { 7068, 218 }, { 7069, 263 }, { 7070, 242 }, { 7071, 207 }, { 7073, 69 },
        { 7077, 223 }, { 7078, 235 }, { 7079, 224 }, { 7080, 179 }, { 7081, 262 }, { 7082, 264 }, { 7083, 142 },
        { 7084, 77 }, { 7085, 147 }, { 7086, 35 }, { 7087, 164 }, { 7088, 176 }, { 7089, 128 }, { 7090, 206 },
        { 7091, 140 }, { 7092, 29 }, { 7093, 204 }, { 7094, 232 }, { 7095, 131 }, { 7096, 270 }, { 7097, 275 },
        { 7098, 236 }, { 7099, 132 }, { 7100, 101 }, { 7102, 67 }, { 7103, 119 }, { 7104, 11 }, { 7109, 21 },
        { 7111, 110 }, { 7112, 155 }, { 7113, 244 }, { 7114, 403 }, { 7115, 120 }, { 7116, 100 }, { 7118, 151 },
        { 7119, 245 }, { 7120, 225 }, { 7121, 169 }, { 7122, 209 }, { 7123, 145 }, { 7124, 266 }, { 7125, 240 },
        { 7126, 256 }, { 7127, 267 }, { 7128, 279 }, { 7129, 191 }, { 7130, 276 }, { 7131, 146 }, { 7132, 252 },
        { 7133, 161 }, { 7134, 185 }, { 7135, 159 }, { 7136, 233 }, { 7137, 246 }, { 7138, 14 }, { 7140, 175 },
        { 7141, 214 }, { 7142, 201 }, { 7143, 125 }, { 7144, 40 }, { 7145, 247 }, { 7146, 178 }, { 7147, 139 },
        { 7148, 208 }, { 7149, 154 }, { 7150, 60 }, { 7151, 205 }, { 7152, 234 }, { 7153, 162 }, { 7154, 44 },
        { 7155, 243 }, { 7156, 227 }, { 7157, 143 }, { 7158, 138 }, { 7159, 46 }, { 7160, 189 }, { 7161, 226 },
        { 7162, 152 }, { 7163, 278 }, { 7164, 216 }, { 7166, 126 }, { 7168, 196 }, { 7171, 149 }, { 7172, 273 },
        { 7173, 32 }, { 7175, 217 }, { 7176, 141 }, { 7177, 238 }, { 7178, 137 }, { 7179, 153 }, { 7180, 190 },
        { 7182, 63 }, { 7183, 47 }, { 7186, 79 }, { 7187, 80 }, { 7188, 109 }, { 7189, 9 }, { 7194, 220 },
        { 7195, 200 }, { 7196, 222 }, { 7197, 198 }, { 7198, 219 }, { 7201, 202 }, { 7202, 188 }, { 7203, 203 },
        { 7204, 71 }, { 7205, 181 }, { 7206, 280 }, { 7207, 197 }, { 7208, 249 }, { 7209, 250 }, { 7211, 221 },
        { 7212, 160 }, { 7213, 166 }, { 7214, 49 }, { 7215, 186 }, { 7216, 184 }, { 7217, 187 }, { 7218, 167 },
        { 7219, 231 }, { 7220, 33 }, { 7221, 168 }, { 7222, 213 }, { 7223, 274 }, { 7224, 260 }, { 7244, 284 },
        { 7245, 237 }, { 7246, 291 }, { 7247, 172 }, { 7248, 290 }, { 7249, 325 }, { 7250, 327 }, { 7251, 324 },
        { 7252, 326 }, { 7260, 319 }, { 7261, 282 }, { 7262, 316 }, { 7263, 289 }, { 7269, 292 }, { 7270, 288 },
        { 7271, 301 }, { 7272, 323 }, { 7273, 277 }, { 7275, 318 }, { 7279, 299 }, { 7280, 300 }, { 7282, 305 },
        { 7283, 295 }, { 7286, 354 }, { 7287, 351 }, { 7288, 353 }, { 7289, 352 }, { 7290, 303 }, { 7292, 307 },
        { 7294, 296 }, { 7296, 359 }, { 7297, 268 }, { 7298, 317 }, { 7299, 361 }, { 7300, 321 }, { 7301, 315 },
        { 7304, 343 }, { 7305, 334 }, { 7306, 332 }, { 7307, 333 }, { 7308, 285 }, { 7309, 335 }, { 7310, 330 },
        { 7311, 298 }, { 7314, 347 }, { 7315, 344 }, { 7316, 257 }, { 7320, 283 }, { 7321, 254 }, { 7322, 375 },
        { 7324, 345 }, { 7325, 340 }, { 7326, 358 }, { 7327, 293 }, { 7328, 355 }, { 7329, 304 }, { 7330, 329 },
        { 7331, 379 }, { 7332, 346 }, { 7333, 378 }, { 7334, 342 }, { 7335, 306 }, { 7336, 311 }, { 7337, 386 },
        { 7338, 377 }, { 7339, 415 }, { 7340, 322 }, { 7341, 401 }, { 7342, 294 }, { 7343, 308 }, { 7344, 393 },
        { 7345, 369 }, { 7346, 312 }, { 7347, 376 }, { 7348, 339 }, { 7349, 259 }, { 7354, 349 }, { 7355, 370 },
        { 7356, 447 }, { 7358, 388 }, { 7359, 402 }, { 7360, 419 }, { 7361, 446 }, { 7362, 418 }, { 7363, 395 },
        { 7364, 435 }, { 7365, 439 }, { 7366, 258 }, { 7368, 399 }, { 7370, 449 }, { 7371, 452 }, { 7372, 374 },
        { 7373, 434 }, { 7374, 182 }, { 7375, 420 }, { 7376, 440 }, { 7377, 286 }, { 7378, 453 }, { 7379, 443 },
        { 7380, 414 }, { 7381, 348 }, { 7383, 391 }, { 7384, 385 }, { 7385, 454 }, { 7386, 467 }, { 7387, 450 },
        { 7388, 462 }, { 7389, 468 }, { 7390, 390 }, { 7391, 456 }, { 7392, 520 }, { 7393, 469 }, { 7395, 513 },
        { 7396, 400 }, { 7397, 371 }, { 7399, 297 }, { 7400, 444 }, { 7401, 514 }, { 7402, 485 }, { 7403, 474 },
        { 7404, 438 }, { 7405, 542 }, { 7406, 445 }, { 7407, 516 }, { 7408, 484 }, { 7409, 545 }, { 7410, 543 },
        { 7411, 463 }, { 7412, 518 }, { 7413, 517 }, { 7414, 566 }, { 7415, 404 }, { 7417, 512 }, { 7418, 320 },
        { 7419, 525 }, { 7420, 527 }, { 7421, 568 }, { 7422, 529 }, { 7423, 309 }, { 7425, 544 }, { 7426, 574 },
        { 7427, 448 }, { 7429, 464 }, { 7430, 560 }, { 7432, 457 }, { 7433, 571 }, { 7434, 573 }, { 7435, 565 },
        { 7436, 587 }, { 7439, 589 }, { 7440, 458 }, { 7441, 515 }, { 7442, 561 }, { 7444, 564 }, { 7445, 585 } };

    for( int[] theaterData : data )
    {
      String idVista = String.valueOf( theaterData[1] );
      int idTheaterDBS = theaterData[0];

      importData( idVista, idTheaterDBS );
    }

    Date end = new Date();

    System.out.println( "--------------------------" );
    System.out.println( ini );
    System.out.println( end );
    System.out.println( end.getTime() - ini.getTime() + " ms" );
    System.out.println( "--------------------------" );
  }

  @Test
  @Ignore
  public void testT()
  {
    List<String> cinema = new ArrayList<String>();
    int i = 0;
    for( int[] theaterData : getData() )
    {
      String idVista = String.valueOf( theaterData[1] );
      int idTheaterDBS = theaterData[0];

      TheaterTO theater = (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( this.theaterDAO
          .findByIdVistaAndActive( idVista ).get( 0 ) );
      String s = theater.getId() + "\t" + idTheaterDBS + "\t" + idVista;
      cinema.add( s );
      System.out.print( "." );
      if( ++i == 40 )
      {
        i = 0;
        System.out.println();
      }
    }

    for( String s : cinema )
    {
      System.out.println( s );
    }
  }

  @Test
  @Ignore
  public void testB()
  {
    List<Integer> registries = Arrays.asList( 7061, 7176, 7179, 7201, 7205, 7207, 7216, 7219, 7262, 7269, 7275, 7306,
      7307, 7337, 7356, 7362, 7363, 7401, 7417, 7168 );

    List<String> tests = new ArrayList<String>();
    int i = 0;
    for( int[] data : getData() )
    {
      int idVista = data[0];
//      if( !registries.contains( idVista ) )
//      {

        NumberFormat nf = new DecimalFormat( "000" );
        String test = "@Test public void test" + nf.format( i ) + "() throws SQLException  { execute( " + i
            + " );  } // idVista:" + idVista;
        tests.add( test );
//      }

      i++;
    }
    Collections.sort( tests );
    for( String test : tests )
    {
      System.out.println( test );
    }

  }

  private int[][] getData()
  {
    // 7363, 7176
    return new int[][] { { 7050, 193 }, { 7051, 194 }, { 7052, 158 }, { 7053, 180 }, { 7054, 183 }, { 7057, 156 },
        { 7058, 212 }, { 7059, 165 }, { 7060, 173 }, { 7061, 255 }, { 7062, 261 }, { 7063, 265 }, { 7064, 241 },
        { 7065, 177 }, { 7067, 171 }, { 7068, 218 }, { 7069, 263 }, { 7070, 242 }, { 7071, 207 }, { 7073, 69 },
        { 7077, 223 }, { 7078, 235 }, { 7079, 224 }, { 7080, 179 }, { 7081, 262 }, { 7082, 264 }, { 7083, 142 },
        { 7084, 77 }, { 7085, 147 }, { 7086, 35 }, { 7087, 164 }, { 7088, 176 }, { 7089, 128 }, { 7090, 206 },
        { 7091, 140 }, { 7092, 29 }, { 7093, 204 }, { 7094, 232 }, { 7095, 131 }, { 7096, 270 }, { 7097, 275 },
        { 7098, 236 }, { 7099, 132 }, { 7100, 101 }, { 7102, 67 }, { 7103, 119 }, { 7104, 11 }, { 7109, 21 },
        { 7111, 110 }, { 7112, 155 }, { 7113, 244 }, { 7114, 403 }, { 7115, 120 }, { 7116, 100 }, { 7118, 151 },
        { 7119, 245 }, { 7120, 225 }, { 7121, 169 }, { 7122, 209 }, { 7123, 145 }, { 7124, 266 }, { 7125, 240 },
        { 7126, 256 }, { 7127, 267 }, { 7128, 279 }, { 7129, 191 }, { 7130, 276 }, { 7131, 146 }, { 7132, 252 },
        { 7133, 161 }, { 7134, 185 }, { 7135, 159 }, { 7136, 233 }, { 7137, 246 }, { 7138, 182 }, { 7140, 175 },
        { 7141, 214 }, { 7142, 201 }, { 7143, 125 }, { 7144, 40 }, { 7145, 247 }, { 7146, 178 }, { 7147, 139 },
        { 7148, 208 }, { 7149, 154 }, { 7150, 60 }, { 7151, 205 }, { 7152, 234 }, { 7153, 162 }, { 7154, 44 },
        { 7155, 243 }, { 7156, 227 }, { 7157, 143 }, { 7158, 138 }, { 7159, 46 }, { 7160, 189 }, { 7161, 226 },
        { 7162, 152 }, { 7163, 278 }, { 7164, 216 }, { 7166, 126 }, { 7168, 196 }, { 7171, 149 }, { 7172, 273 },
        { 7173, 32 }, { 7175, 217 }, { 7176, 141 }, { 7177, 238 }, { 7178, 137 }, { 7179, 153 }, { 7180, 190 },
        { 7182, 63 }, { 7183, 47 }, { 7186, 79 }, { 7187, 80 }, { 7188, 109 }, { 7189, 9 }, { 7194, 220 },
        { 7195, 200 }, { 7196, 222 }, { 7197, 198 }, { 7198, 219 }, { 7201, 202 }, { 7202, 188 }, { 7203, 203 },
        { 7204, 71 }, { 7205, 181 }, { 7206, 280 }, { 7207, 197 }, { 7208, 249 }, { 7209, 250 }, { 7211, 221 },
        { 7212, 160 }, { 7213, 166 }, { 7214, 49 }, { 7215, 186 }, { 7216, 184 }, { 7217, 187 }, { 7218, 167 },
        { 7219, 231 }, { 7220, 33 }, { 7221, 168 }, { 7222, 213 }, { 7223, 274 }, { 7224, 260 }, { 7244, 284 },
        { 7245, 237 }, { 7246, 291 }, { 7247, 172 }, { 7248, 290 }, { 7249, 325 }, { 7250, 327 }, { 7251, 324 },
        { 7252, 326 }, { 7260, 319 }, { 7261, 282 }, { 7262, 316 }, { 7263, 289 }, { 7269, 292 }, { 7270, 288 },
        { 7271, 301 }, { 7272, 323 }, { 7273, 277 }, { 7275, 318 }, { 7279, 299 }, { 7280, 300 }, { 7282, 305 },
        { 7283, 295 }, { 7286, 354 }, { 7287, 351 }, { 7288, 353 }, { 7289, 352 }, { 7290, 303 }, { 7292, 307 },
        { 7294, 296 }, { 7296, 359 }, { 7297, 268 }, { 7298, 317 }, { 7299, 361 }, { 7300, 321 }, { 7301, 315 },
        { 7304, 343 }, { 7305, 334 }, { 7306, 332 }, { 7307, 333 }, { 7308, 285 }, { 7309, 335 }, { 7310, 330 },
        { 7311, 298 }, { 7314, 347 }, { 7315, 344 }, { 7316, 257 }, { 7320, 283 }, { 7321, 254 }, { 7322, 375 },
        { 7324, 345 }, { 7325, 340 }, { 7326, 358 }, { 7327, 293 }, { 7328, 355 }, { 7329, 304 }, { 7330, 329 },
        { 7331, 379 }, { 7332, 346 }, { 7333, 378 }, { 7334, 342 }, { 7335, 306 }, { 7336, 311 }, { 7337, 386 },
        { 7338, 377 }, { 7339, 415 }, { 7340, 322 }, { 7341, 401 }, { 7342, 294 }, { 7343, 308 }, { 7344, 393 },
        { 7345, 369 }, { 7346, 312 }, { 7347, 376 }, { 7348, 339 }, { 7349, 259 }, { 7354, 349 }, { 7355, 370 },
        { 7356, 447 }, { 7358, 388 }, { 7359, 402 }, { 7360, 419 }, { 7361, 446 }, { 7362, 418 }, { 7363, 395 },
        { 7364, 435 }, { 7365, 439 }, { 7366, 258 }, { 7368, 399 }, { 7370, 449 }, { 7371, 452 }, { 7372, 374 },
        { 7373, 434 }, { 7374, 272 }, { 7375, 420 }, { 7376, 440 }, { 7377, 286 }, { 7378, 453 }, { 7379, 443 },
        { 7380, 414 }, { 7381, 348 }, { 7383, 391 }, { 7384, 385 }, { 7385, 454 }, { 7386, 467 }, { 7387, 450 },
        { 7388, 462 }, { 7389, 468 }, { 7390, 390 }, { 7391, 456 }, { 7392, 520 }, { 7393, 469 }, { 7395, 513 },
        { 7396, 400 }, { 7397, 371 }, { 7399, 297 }, { 7400, 444 }, { 7401, 514 }, { 7402, 485 }, { 7403, 474 },
        { 7404, 438 }, { 7405, 542 }, { 7406, 445 }, { 7407, 516 }, { 7408, 484 }, { 7409, 545 }, { 7410, 543 },
        { 7411, 463 }, { 7412, 518 }, { 7413, 517 }, { 7414, 566 }, { 7415, 404 }, { 7417, 512 }, { 7418, 320 },
        { 7419, 525 }, { 7420, 527 }, { 7421, 568 }, { 7422, 529 }, { 7423, 309 }, { 7425, 544 }, { 7426, 574 },
        { 7427, 448 }, { 7429, 464 }, { 7430, 560 }, { 7432, 457 }, { 7433, 571 }, { 7434, 573 }, { 7435, 565 },
        { 7436, 587 }, { 7439, 589 }, { 7440, 458 }, { 7441, 515 }, { 7442, 561 }, { 7444, 564 }, { 7443, 585 } };
  }

  @Test
  public void testA()
  {
    int i = 0;
    for( int[] data : getData() )
    {
      if( data[0] == 7443)
      {
        System.out.println( i );
      } else {
        
      }
      i++;
    }
  }
  
  @Test public void test000() throws SQLException  { execute( 0 );  } // idVista:7050
  @Test public void test001() throws SQLException  { execute( 1 );  } // idVista:7051
  @Test public void test002() throws SQLException  { execute( 2 );  } // idVista:7052
  @Test public void test003() throws SQLException  { execute( 3 );  } // idVista:7053
  @Test public void test004() throws SQLException  { execute( 4 );  } // idVista:7054
  @Test public void test005() throws SQLException  { execute( 5 );  } // idVista:7057
  @Test public void test006() throws SQLException  { execute( 6 );  } // idVista:7058
  @Test public void test007() throws SQLException  { execute( 7 );  } // idVista:7059
  @Test public void test008() throws SQLException  { execute( 8 );  } // idVista:7060
  @Test public void test009() throws SQLException  { execute( 9 );  } // idVista:7061
  @Test public void test010() throws SQLException  { execute( 10 );  } // idVista:7062
  @Test public void test011() throws SQLException  { execute( 11 );  } // idVista:7063
  @Test public void test012() throws SQLException  { execute( 12 );  } // idVista:7064
  @Test public void test013() throws SQLException  { execute( 13 );  } // idVista:7065
  @Test public void test014() throws SQLException  { execute( 14 );  } // idVista:7067
  @Test public void test015() throws SQLException  { execute( 15 );  } // idVista:7068
  @Test public void test016() throws SQLException  { execute( 16 );  } // idVista:7069
  @Test public void test017() throws SQLException  { execute( 17 );  } // idVista:7070
  @Test public void test018() throws SQLException  { execute( 18 );  } // idVista:7071
  @Test public void test019() throws SQLException  { execute( 19 );  } // idVista:7073
  @Test public void test020() throws SQLException  { execute( 20 );  } // idVista:7077
  @Test public void test021() throws SQLException  { execute( 21 );  } // idVista:7078
  @Test public void test022() throws SQLException  { execute( 22 );  } // idVista:7079
  @Test public void test023() throws SQLException  { execute( 23 );  } // idVista:7080
  @Test public void test024() throws SQLException  { execute( 24 );  } // idVista:7081
  @Test public void test025() throws SQLException  { execute( 25 );  } // idVista:7082
  @Test public void test026() throws SQLException  { execute( 26 );  } // idVista:7083
  @Test public void test027() throws SQLException  { execute( 27 );  } // idVista:7084
  @Test public void test028() throws SQLException  { execute( 28 );  } // idVista:7085
  @Test public void test029() throws SQLException  { execute( 29 );  } // idVista:7086
  @Test public void test030() throws SQLException  { execute( 30 );  } // idVista:7087
  @Test public void test031() throws SQLException  { execute( 31 );  } // idVista:7088
  @Test public void test032() throws SQLException  { execute( 32 );  } // idVista:7089
  @Test public void test033() throws SQLException  { execute( 33 );  } // idVista:7090
  @Test public void test034() throws SQLException  { execute( 34 );  } // idVista:7091
  @Test public void test035() throws SQLException  { execute( 35 );  } // idVista:7092
  @Test public void test036() throws SQLException  { execute( 36 );  } // idVista:7093
  @Test public void test037() throws SQLException  { execute( 37 );  } // idVista:7094
  @Test public void test038() throws SQLException  { execute( 38 );  } // idVista:7095
  @Test public void test039() throws SQLException  { execute( 39 );  } // idVista:7096
  @Test public void test040() throws SQLException  { execute( 40 );  } // idVista:7097
  @Test public void test041() throws SQLException  { execute( 41 );  } // idVista:7098
  @Test public void test042() throws SQLException  { execute( 42 );  } // idVista:7099
  @Test public void test043() throws SQLException  { execute( 43 );  } // idVista:7100
  @Test public void test044() throws SQLException  { execute( 44 );  } // idVista:7102
  @Test public void test045() throws SQLException  { execute( 45 );  } // idVista:7103
  @Test public void test046() throws SQLException  { execute( 46 );  } // idVista:7104
  @Test public void test047() throws SQLException  { execute( 47 );  } // idVista:7109
  @Test public void test048() throws SQLException  { execute( 48 );  } // idVista:7111
  @Test public void test049() throws SQLException  { execute( 49 );  } // idVista:7112
  @Test public void test050() throws SQLException  { execute( 50 );  } // idVista:7113
  @Test public void test051() throws SQLException  { execute( 51 );  } // idVista:7114
  @Test public void test052() throws SQLException  { execute( 52 );  } // idVista:7115
  @Test public void test053() throws SQLException  { execute( 53 );  } // idVista:7116
  @Test public void test054() throws SQLException  { execute( 54 );  } // idVista:7118
  @Test public void test055() throws SQLException  { execute( 55 );  } // idVista:7119
  @Test public void test056() throws SQLException  { execute( 56 );  } // idVista:7120
  @Test public void test057() throws SQLException  { execute( 57 );  } // idVista:7121
  @Test public void test058() throws SQLException  { execute( 58 );  } // idVista:7122
  @Test public void test059() throws SQLException  { execute( 59 );  } // idVista:7123
  @Test public void test060() throws SQLException  { execute( 60 );  } // idVista:7124
  @Test public void test061() throws SQLException  { execute( 61 );  } // idVista:7125
  @Test public void test062() throws SQLException  { execute( 62 );  } // idVista:7126
  @Test public void test063() throws SQLException  { execute( 63 );  } // idVista:7127
  @Test public void test064() throws SQLException  { execute( 64 );  } // idVista:7128
  @Test public void test065() throws SQLException  { execute( 65 );  } // idVista:7129
  @Test public void test066() throws SQLException  { execute( 66 );  } // idVista:7130
  @Test public void test067() throws SQLException  { execute( 67 );  } // idVista:7131
  @Test public void test068() throws SQLException  { execute( 68 );  } // idVista:7132
  @Test public void test069() throws SQLException  { execute( 69 );  } // idVista:7133
  @Test public void test070() throws SQLException  { execute( 70 );  } // idVista:7134
  @Test public void test071() throws SQLException  { execute( 71 );  } // idVista:7135
  @Test public void test072() throws SQLException  { execute( 72 );  } // idVista:7136
  @Test public void test073() throws SQLException  { execute( 73 );  } // idVista:7137
  @Test public void test074() throws SQLException  { execute( 74 );  } // idVista:7138
  @Test public void test075() throws SQLException  { execute( 75 );  } // idVista:7140
  @Test public void test076() throws SQLException  { execute( 76 );  } // idVista:7141
  @Test public void test077() throws SQLException  { execute( 77 );  } // idVista:7142
  @Test public void test078() throws SQLException  { execute( 78 );  } // idVista:7143
  @Test public void test079() throws SQLException  { execute( 79 );  } // idVista:7144
  @Test public void test080() throws SQLException  { execute( 80 );  } // idVista:7145
  @Test public void test081() throws SQLException  { execute( 81 );  } // idVista:7146
  @Test public void test082() throws SQLException  { execute( 82 );  } // idVista:7147
  @Test public void test083() throws SQLException  { execute( 83 );  } // idVista:7148
  @Test public void test084() throws SQLException  { execute( 84 );  } // idVista:7149
  @Test public void test085() throws SQLException  { execute( 85 );  } // idVista:7150
  @Test public void test086() throws SQLException  { execute( 86 );  } // idVista:7151
  @Test public void test087() throws SQLException  { execute( 87 );  } // idVista:7152
  @Test public void test088() throws SQLException  { execute( 88 );  } // idVista:7153
  @Test public void test089() throws SQLException  { execute( 89 );  } // idVista:7154
  @Test public void test090() throws SQLException  { execute( 90 );  } // idVista:7155
  @Test public void test091() throws SQLException  { execute( 91 );  } // idVista:7156
  @Test public void test092() throws SQLException  { execute( 92 );  } // idVista:7157
  @Test public void test093() throws SQLException  { execute( 93 );  } // idVista:7158
  @Test public void test094() throws SQLException  { execute( 94 );  } // idVista:7159
  @Test public void test095() throws SQLException  { execute( 95 );  } // idVista:7160
  @Test public void test096() throws SQLException  { execute( 96 );  } // idVista:7161
  @Test public void test097() throws SQLException  { execute( 97 );  } // idVista:7162
  @Test public void test098() throws SQLException  { execute( 98 );  } // idVista:7163
  @Test public void test099() throws SQLException  { execute( 99 );  } // idVista:7164
  @Test public void test100() throws SQLException  { execute( 100 );  } // idVista:7166
  @Test public void test101() throws SQLException  { execute( 101 );  } // idVista:7168
  @Test public void test102() throws SQLException  { execute( 102 );  } // idVista:7171
  @Test public void test103() throws SQLException  { execute( 103 );  } // idVista:7172
  @Test public void test104() throws SQLException  { execute( 104 );  } // idVista:7173
  @Test public void test105() throws SQLException  { execute( 105 );  } // idVista:7175
  @Test public void test106() throws SQLException  { execute( 106 );  } // idVista:7176
  @Test public void test107() throws SQLException  { execute( 107 );  } // idVista:7177
  @Test public void test108() throws SQLException  { execute( 108 );  } // idVista:7178
  @Test public void test109() throws SQLException  { execute( 109 );  } // idVista:7179
  @Test public void test110() throws SQLException  { execute( 110 );  } // idVista:7180
  @Test public void test111() throws SQLException  { execute( 111 );  } // idVista:7182
  @Test public void test112() throws SQLException  { execute( 112 );  } // idVista:7183
  @Test public void test113() throws SQLException  { execute( 113 );  } // idVista:7186
  @Test public void test114() throws SQLException  { execute( 114 );  } // idVista:7187
  @Test public void test115() throws SQLException  { execute( 115 );  } // idVista:7188
  @Test public void test116() throws SQLException  { execute( 116 );  } // idVista:7189
  @Test public void test117() throws SQLException  { execute( 117 );  } // idVista:7194
  @Test public void test118() throws SQLException  { execute( 118 );  } // idVista:7195
  @Test public void test119() throws SQLException  { execute( 119 );  } // idVista:7196
  @Test public void test120() throws SQLException  { execute( 120 );  } // idVista:7197
  @Test public void test121() throws SQLException  { execute( 121 );  } // idVista:7198
  @Test public void test122() throws SQLException  { execute( 122 );  } // idVista:7201
  @Test public void test123() throws SQLException  { execute( 123 );  } // idVista:7202
  @Test public void test124() throws SQLException  { execute( 124 );  } // idVista:7203
  @Test public void test125() throws SQLException  { execute( 125 );  } // idVista:7204
  @Test public void test126() throws SQLException  { execute( 126 );  } // idVista:7205
  @Test public void test127() throws SQLException  { execute( 127 );  } // idVista:7206
  @Test public void test128() throws SQLException  { execute( 128 );  } // idVista:7207
  @Test public void test129() throws SQLException  { execute( 129 );  } // idVista:7208
  @Test public void test130() throws SQLException  { execute( 130 );  } // idVista:7209
  @Test public void test131() throws SQLException  { execute( 131 );  } // idVista:7211
  @Test public void test132() throws SQLException  { execute( 132 );  } // idVista:7212
  @Test public void test133() throws SQLException  { execute( 133 );  } // idVista:7213
  @Test public void test134() throws SQLException  { execute( 134 );  } // idVista:7214
  @Test public void test135() throws SQLException  { execute( 135 );  } // idVista:7215
  @Test public void test136() throws SQLException  { execute( 136 );  } // idVista:7216
  @Test public void test137() throws SQLException  { execute( 137 );  } // idVista:7217
  @Test public void test138() throws SQLException  { execute( 138 );  } // idVista:7218
  @Test public void test139() throws SQLException  { execute( 139 );  } // idVista:7219
  @Test public void test140() throws SQLException  { execute( 140 );  } // idVista:7220
  @Test public void test141() throws SQLException  { execute( 141 );  } // idVista:7221
  @Test public void test142() throws SQLException  { execute( 142 );  } // idVista:7222
  @Test public void test143() throws SQLException  { execute( 143 );  } // idVista:7223
  @Test public void test144() throws SQLException  { execute( 144 );  } // idVista:7224
  @Test public void test145() throws SQLException  { execute( 145 );  } // idVista:7244
  @Test public void test146() throws SQLException  { execute( 146 );  } // idVista:7245
  @Test public void test147() throws SQLException  { execute( 147 );  } // idVista:7246
  @Test public void test148() throws SQLException  { execute( 148 );  } // idVista:7247
  @Test public void test149() throws SQLException  { execute( 149 );  } // idVista:7248
  @Test public void test150() throws SQLException  { execute( 150 );  } // idVista:7249
  @Test public void test151() throws SQLException  { execute( 151 );  } // idVista:7250
  @Test public void test152() throws SQLException  { execute( 152 );  } // idVista:7251
  @Test public void test153() throws SQLException  { execute( 153 );  } // idVista:7252
  @Test public void test154() throws SQLException  { execute( 154 );  } // idVista:7260
  @Test public void test155() throws SQLException  { execute( 155 );  } // idVista:7261
  @Test public void test156() throws SQLException  { execute( 156 );  } // idVista:7262
  @Test public void test157() throws SQLException  { execute( 157 );  } // idVista:7263
  @Test public void test158() throws SQLException  { execute( 158 );  } // idVista:7269
  @Test public void test159() throws SQLException  { execute( 159 );  } // idVista:7270
  @Test public void test160() throws SQLException  { execute( 160 );  } // idVista:7271
  @Test public void test161() throws SQLException  { execute( 161 );  } // idVista:7272
  @Test public void test162() throws SQLException  { execute( 162 );  } // idVista:7273
  @Test public void test163() throws SQLException  { execute( 163 );  } // idVista:7275
  @Test public void test164() throws SQLException  { execute( 164 );  } // idVista:7279
  @Test public void test165() throws SQLException  { execute( 165 );  } // idVista:7280
  @Test public void test166() throws SQLException  { execute( 166 );  } // idVista:7282
  @Test public void test167() throws SQLException  { execute( 167 );  } // idVista:7283
  @Test public void test168() throws SQLException  { execute( 168 );  } // idVista:7286
  @Test public void test169() throws SQLException  { execute( 169 );  } // idVista:7287
  @Test public void test170() throws SQLException  { execute( 170 );  } // idVista:7288
  @Test public void test171() throws SQLException  { execute( 171 );  } // idVista:7289
  @Test public void test172() throws SQLException  { execute( 172 );  } // idVista:7290
  @Test public void test173() throws SQLException  { execute( 173 );  } // idVista:7292
  @Test public void test174() throws SQLException  { execute( 174 );  } // idVista:7294
  @Test public void test175() throws SQLException  { execute( 175 );  } // idVista:7296
  @Test public void test176() throws SQLException  { execute( 176 );  } // idVista:7297
  @Test public void test177() throws SQLException  { execute( 177 );  } // idVista:7298
  @Test public void test178() throws SQLException  { execute( 178 );  } // idVista:7299
  @Test public void test179() throws SQLException  { execute( 179 );  } // idVista:7300
  @Test public void test180() throws SQLException  { execute( 180 );  } // idVista:7301
  @Test public void test181() throws SQLException  { execute( 181 );  } // idVista:7304
  @Test public void test182() throws SQLException  { execute( 182 );  } // idVista:7305
  @Test public void test183() throws SQLException  { execute( 183 );  } // idVista:7306
  @Test public void test184() throws SQLException  { execute( 184 );  } // idVista:7307
  @Test public void test185() throws SQLException  { execute( 185 );  } // idVista:7308
  @Test public void test186() throws SQLException  { execute( 186 );  } // idVista:7309
  @Test public void test187() throws SQLException  { execute( 187 );  } // idVista:7310
  @Test public void test188() throws SQLException  { execute( 188 );  } // idVista:7311
  @Test public void test189() throws SQLException  { execute( 189 );  } // idVista:7314
  @Test public void test190() throws SQLException  { execute( 190 );  } // idVista:7315
  @Test public void test191() throws SQLException  { execute( 191 );  } // idVista:7316
  @Test public void test192() throws SQLException  { execute( 192 );  } // idVista:7320
  @Test public void test193() throws SQLException  { execute( 193 );  } // idVista:7321
  @Test public void test194() throws SQLException  { execute( 194 );  } // idVista:7322
  @Test public void test195() throws SQLException  { execute( 195 );  } // idVista:7324
  @Test public void test196() throws SQLException  { execute( 196 );  } // idVista:7325
  @Test public void test197() throws SQLException  { execute( 197 );  } // idVista:7326
  @Test public void test198() throws SQLException  { execute( 198 );  } // idVista:7327
  @Test public void test199() throws SQLException  { execute( 199 );  } // idVista:7328
  @Test public void test200() throws SQLException  { execute( 200 );  } // idVista:7329
  @Test public void test201() throws SQLException  { execute( 201 );  } // idVista:7330
  @Test public void test202() throws SQLException  { execute( 202 );  } // idVista:7331
  @Test public void test203() throws SQLException  { execute( 203 );  } // idVista:7332
  @Test public void test204() throws SQLException  { execute( 204 );  } // idVista:7333
  @Test public void test205() throws SQLException  { execute( 205 );  } // idVista:7334
  @Test public void test206() throws SQLException  { execute( 206 );  } // idVista:7335
  @Test public void test207() throws SQLException  { execute( 207 );  } // idVista:7336
  @Test public void test208() throws SQLException  { execute( 208 );  } // idVista:7337
  @Test public void test209() throws SQLException  { execute( 209 );  } // idVista:7338
  @Test public void test210() throws SQLException  { execute( 210 );  } // idVista:7339
  @Test public void test211() throws SQLException  { execute( 211 );  } // idVista:7340
  @Test public void test212() throws SQLException  { execute( 212 );  } // idVista:7341
  @Test public void test213() throws SQLException  { execute( 213 );  } // idVista:7342
  @Test public void test214() throws SQLException  { execute( 214 );  } // idVista:7343
  @Test public void test215() throws SQLException  { execute( 215 );  } // idVista:7344
  @Test public void test216() throws SQLException  { execute( 216 );  } // idVista:7345
  @Test public void test217() throws SQLException  { execute( 217 );  } // idVista:7346
  @Test public void test218() throws SQLException  { execute( 218 );  } // idVista:7347
  @Test public void test219() throws SQLException  { execute( 219 );  } // idVista:7348
  @Test public void test220() throws SQLException  { execute( 220 );  } // idVista:7349
  @Test public void test221() throws SQLException  { execute( 221 );  } // idVista:7354
  @Test public void test222() throws SQLException  { execute( 222 );  } // idVista:7355
  @Test public void test223() throws SQLException  { execute( 223 );  } // idVista:7356
  @Test public void test224() throws SQLException  { execute( 224 );  } // idVista:7358
  @Test public void test225() throws SQLException  { execute( 225 );  } // idVista:7359
  @Test public void test226() throws SQLException  { execute( 226 );  } // idVista:7360
  @Test public void test227() throws SQLException  { execute( 227 );  } // idVista:7361
  @Test public void test228() throws SQLException  { execute( 228 );  } // idVista:7362
  @Test public void test229() throws SQLException  { execute( 229 );  } // idVista:7363
  @Test public void test230() throws SQLException  { execute( 230 );  } // idVista:7364
  @Test public void test231() throws SQLException  { execute( 231 );  } // idVista:7365
  @Test public void test232() throws SQLException  { execute( 232 );  } // idVista:7366
  @Test public void test233() throws SQLException  { execute( 233 );  } // idVista:7368
  @Test public void test234() throws SQLException  { execute( 234 );  } // idVista:7370
  @Test public void test235() throws SQLException  { execute( 235 );  } // idVista:7371
  @Test public void test236() throws SQLException  { execute( 236 );  } // idVista:7372
  @Test public void test237() throws SQLException  { execute( 237 );  } // idVista:7373
  @Test public void test238() throws SQLException  { execute( 238 );  } // idVista:7374
  @Test public void test239() throws SQLException  { execute( 239 );  } // idVista:7375
  @Test public void test240() throws SQLException  { execute( 240 );  } // idVista:7376
  @Test public void test241() throws SQLException  { execute( 241 );  } // idVista:7377
  @Test public void test242() throws SQLException  { execute( 242 );  } // idVista:7378
  @Test public void test243() throws SQLException  { execute( 243 );  } // idVista:7379
  @Test public void test244() throws SQLException  { execute( 244 );  } // idVista:7380
  @Test public void test245() throws SQLException  { execute( 245 );  } // idVista:7381
  @Test public void test246() throws SQLException  { execute( 246 );  } // idVista:7383
  @Test public void test247() throws SQLException  { execute( 247 );  } // idVista:7384
  @Test public void test248() throws SQLException  { execute( 248 );  } // idVista:7385
  @Test public void test249() throws SQLException  { execute( 249 );  } // idVista:7386
  @Test public void test250() throws SQLException  { execute( 250 );  } // idVista:7387
  @Test public void test251() throws SQLException  { execute( 251 );  } // idVista:7388
  @Test public void test252() throws SQLException  { execute( 252 );  } // idVista:7389
  @Test public void test253() throws SQLException  { execute( 253 );  } // idVista:7390
  @Test public void test254() throws SQLException  { execute( 254 );  } // idVista:7391
  @Test public void test255() throws SQLException  { execute( 255 );  } // idVista:7392
  @Test public void test256() throws SQLException  { execute( 256 );  } // idVista:7393
  @Test public void test257() throws SQLException  { execute( 257 );  } // idVista:7395
  @Test public void test258() throws SQLException  { execute( 258 );  } // idVista:7396
  @Test public void test259() throws SQLException  { execute( 259 );  } // idVista:7397
  @Test public void test260() throws SQLException  { execute( 260 );  } // idVista:7399
  @Test public void test261() throws SQLException  { execute( 261 );  } // idVista:7400
  @Test public void test262() throws SQLException  { execute( 262 );  } // idVista:7401
  @Test public void test263() throws SQLException  { execute( 263 );  } // idVista:7402
  @Test public void test264() throws SQLException  { execute( 264 );  } // idVista:7403
  @Test public void test265() throws SQLException  { execute( 265 );  } // idVista:7404
  @Test public void test266() throws SQLException  { execute( 266 );  } // idVista:7405
  @Test public void test267() throws SQLException  { execute( 267 );  } // idVista:7406
  @Test public void test268() throws SQLException  { execute( 268 );  } // idVista:7407
  @Test public void test269() throws SQLException  { execute( 269 );  } // idVista:7408
  @Test public void test270() throws SQLException  { execute( 270 );  } // idVista:7409
  @Test public void test271() throws SQLException  { execute( 271 );  } // idVista:7410
  @Test public void test272() throws SQLException  { execute( 272 );  } // idVista:7411
  @Test public void test273() throws SQLException  { execute( 273 );  } // idVista:7412
  @Test public void test274() throws SQLException  { execute( 274 );  } // idVista:7413
  @Test public void test275() throws SQLException  { execute( 275 );  } // idVista:7414
  @Test public void test276() throws SQLException  { execute( 276 );  } // idVista:7415
  @Test public void test277() throws SQLException  { execute( 277 );  } // idVista:7417
  @Test public void test278() throws SQLException  { execute( 278 );  } // idVista:7418
  @Test public void test279() throws SQLException  { execute( 279 );  } // idVista:7419
  @Test public void test280() throws SQLException  { execute( 280 );  } // idVista:7420
  @Test public void test281() throws SQLException  { execute( 281 );  } // idVista:7421
  @Test public void test282() throws SQLException  { execute( 282 );  } // idVista:7422
  @Test public void test283() throws SQLException  { execute( 283 );  } // idVista:7423
  @Test public void test284() throws SQLException  { execute( 284 );  } // idVista:7425
  @Test public void test285() throws SQLException  { execute( 285 );  } // idVista:7426
  @Test public void test286() throws SQLException  { execute( 286 );  } // idVista:7427
  @Test public void test287() throws SQLException  { execute( 287 );  } // idVista:7429
  @Test public void test288() throws SQLException  { execute( 288 );  } // idVista:7430
  @Test public void test289() throws SQLException  { execute( 289 );  } // idVista:7432
  @Test public void test290() throws SQLException  { execute( 290 );  } // idVista:7433
  @Test public void test291() throws SQLException  { execute( 291 );  } // idVista:7434
  @Test public void test292() throws SQLException  { execute( 292 );  } // idVista:7435
  @Test public void test293() throws SQLException  { execute( 293 );  } // idVista:7436
  @Test public void test294() throws SQLException  { execute( 294 );  } // idVista:7439
  @Test public void test295() throws SQLException  { execute( 295 );  } // idVista:7440
  @Test public void test296() throws SQLException  { execute( 296 );  } // idVista:7441
  @Test public void test297() throws SQLException  { execute( 297 );  } // idVista:7442
  @Test public void test298() throws SQLException  { execute( 298 );  } // idVista:7444
  @Test public void test299() throws SQLException  { execute( 299 );  } // idVista:7443


  private void execute( int id )
  {
    try
    {
      Date ini = new Date();
      int[] theaterData = this.getData()[id];
      String idVista = String.valueOf( theaterData[1] );
      int idTheaterDBS = theaterData[0];

      System.out.println( "Test " + id + ", idTheaterDBS: " + idTheaterDBS + ", idVista:" + idVista );

      importData( idVista, idTheaterDBS );

      Date end = new Date();

      System.out.println( "--------------------------" );
      System.out.println( ini );
      System.out.println( end );
      System.out.println( end.getTime() - ini.getTime() + " ms" );
      System.out.println( "--------------------------" );
    }
    catch( SQLException e )
    {
      e.printStackTrace();
    }
  }

  /*
   * @Test public void test0() throws SQLException { execute( 0 ); }
   * @Test public void test1() throws SQLException { execute( 1 ); }
   * @Test public void test2() throws SQLException { execute( 2 ); }
   * @Test public void test3() throws SQLException { execute( 3 ); }
   * @Test public void test4() throws SQLException { execute( 4 ); }
   * @Test public void test5() throws SQLException { execute( 5 ); }
   * @Test public void test6() throws SQLException { execute( 6 ); }
   * @Test public void test7() throws SQLException { execute( 7 ); }
   * @Test public void test8() throws SQLException { execute( 8 ); }
   * @Test public void test9() throws SQLException { execute( 9 ); }
   * @Test public void test10() throws SQLException { execute( 10 ); }
   * @Test public void test11() throws SQLException { execute( 11 ); }
   * @Test public void test12() throws SQLException { execute( 12 ); }
   * @Test public void test13() throws SQLException { execute( 13 ); }
   * @Test public void test14() throws SQLException { execute( 14 ); }
   * @Test public void test15() throws SQLException { execute( 15 ); }
   * @Test public void test16() throws SQLException { execute( 16 ); }
   * @Test public void test17() throws SQLException { execute( 17 ); }
   * @Test public void test18() throws SQLException { execute( 18 ); }
   * @Test public void test19() throws SQLException { execute( 19 ); }
   * @Test public void test20() throws SQLException { execute( 20 ); }
   * @Test public void test21() throws SQLException { execute( 21 ); }
   * @Test public void test22() throws SQLException { execute( 22 ); }
   * @Test public void test23() throws SQLException { execute( 23 ); }
   * @Test public void test24() throws SQLException { execute( 24 ); }
   * @Test public void test25() throws SQLException { execute( 25 ); }
   * @Test public void test26() throws SQLException { execute( 26 ); }
   * @Test public void test27() throws SQLException { execute( 27 ); }
   * @Test public void test28() throws SQLException { execute( 28 ); }
   * @Test public void test29() throws SQLException { execute( 29 ); }
   * @Test public void test30() throws SQLException { execute( 30 ); }
   * @Test public void test31() throws SQLException { execute( 31 ); }
   * @Test public void test32() throws SQLException { execute( 32 ); }
   * @Test public void test33() throws SQLException { execute( 33 ); }
   * @Test public void test34() throws SQLException { execute( 34 ); }
   * @Test public void test35() throws SQLException { execute( 35 ); }
   * @Test public void test36() throws SQLException { execute( 36 ); }
   * @Test public void test37() throws SQLException { execute( 37 ); }
   * @Test public void test38() throws SQLException { execute( 38 ); }
   * @Test public void test39() throws SQLException { execute( 39 ); }
   * @Test public void test40() throws SQLException { execute( 40 ); }
   * @Test public void test41() throws SQLException { execute( 41 ); }
   * @Test public void test42() throws SQLException { execute( 42 ); }
   * @Test public void test43() throws SQLException { execute( 43 ); }
   * @Test public void test44() throws SQLException { execute( 44 ); }
   * @Test public void test45() throws SQLException { execute( 45 ); }
   * @Test public void test46() throws SQLException { execute( 46 ); }
   * @Test public void test47() throws SQLException { execute( 47 ); }
   * @Test public void test48() throws SQLException { execute( 48 ); }
   * @Test public void test49() throws SQLException { execute( 49 ); }
   * @Test public void test50() throws SQLException { execute( 50 ); }
   * @Test public void test51() throws SQLException { execute( 51 ); }
   * @Test public void test52() throws SQLException { execute( 52 ); }
   * @Test public void test53() throws SQLException { execute( 53 ); }
   * @Test public void test54() throws SQLException { execute( 54 ); }
   * @Test public void test55() throws SQLException { execute( 55 ); }
   * @Test public void test56() throws SQLException { execute( 56 ); }
   * @Test public void test57() throws SQLException { execute( 57 ); }
   * @Test public void test58() throws SQLException { execute( 58 ); }
   * @Test public void test59() throws SQLException { execute( 59 ); }
   * @Test public void test60() throws SQLException { execute( 60 ); }
   * @Test public void test61() throws SQLException { execute( 61 ); }
   * @Test public void test62() throws SQLException { execute( 62 ); }
   * @Test public void test63() throws SQLException { execute( 63 ); }
   * @Test public void test64() throws SQLException { execute( 64 ); }
   * @Test public void test65() throws SQLException { execute( 65 ); }
   * @Test public void test66() throws SQLException { execute( 66 ); }
   * @Test public void test67() throws SQLException { execute( 67 ); }
   * @Test public void test68() throws SQLException { execute( 68 ); }
   * @Test public void test69() throws SQLException { execute( 69 ); }
   * @Test public void test70() throws SQLException { execute( 70 ); }
   * @Test public void test71() throws SQLException { execute( 71 ); }
   * @Test public void test72() throws SQLException { execute( 72 ); }
   * @Test public void test73() throws SQLException { execute( 73 ); }
   * @Test public void test74() throws SQLException { execute( 74 ); }
   * @Test public void test75() throws SQLException { execute( 75 ); }
   * @Test public void test76() throws SQLException { execute( 76 ); }
   * @Test public void test77() throws SQLException { execute( 77 ); }
   * @Test public void test78() throws SQLException { execute( 78 ); }
   * @Test public void test79() throws SQLException { execute( 79 ); }
   * @Test public void test80() throws SQLException { execute( 80 ); }
   * @Test public void test81() throws SQLException { execute( 81 ); }
   * @Test public void test82() throws SQLException { execute( 82 ); }
   * @Test public void test83() throws SQLException { execute( 83 ); }
   * @Test public void test84() throws SQLException { execute( 84 ); }
   * @Test public void test85() throws SQLException { execute( 85 ); }
   * @Test public void test86() throws SQLException { execute( 86 ); }
   * @Test public void test87() throws SQLException { execute( 87 ); }
   * @Test public void test88() throws SQLException { execute( 88 ); }
   * @Test public void test89() throws SQLException { execute( 89 ); }
   * @Test public void test90() throws SQLException { execute( 90 ); }
   * @Test public void test91() throws SQLException { execute( 91 ); }
   * @Test public void test92() throws SQLException { execute( 92 ); }
   * @Test public void test93() throws SQLException { execute( 93 ); }
   * @Test public void test94() throws SQLException { execute( 94 ); }
   * @Test public void test95() throws SQLException { execute( 95 ); }
   * @Test public void test96() throws SQLException { execute( 96 ); }
   * @Test public void test97() throws SQLException { execute( 97 ); }
   * @Test public void test98() throws SQLException { execute( 98 ); }
   * @Test public void test99() throws SQLException { execute( 99 ); }
   * @Test public void test100() throws SQLException { execute( 100 ); }
   * @Test public void test101() throws SQLException { execute( 101 ); }
   * @Test public void test102() throws SQLException { execute( 102 ); }
   * @Test public void test103() throws SQLException { execute( 103 ); }
   * @Test public void test104() throws SQLException { execute( 104 ); }
   * @Test public void test105() throws SQLException { execute( 105 ); }
   * @Test public void test106() throws SQLException { execute( 106 ); }
   * @Test public void test107() throws SQLException { execute( 107 ); }
   * @Test public void test108() throws SQLException { execute( 108 ); }
   * @Test public void test109() throws SQLException { execute( 109 ); }
   * @Test public void test110() throws SQLException { execute( 110 ); }
   * @Test public void test111() throws SQLException { execute( 111 ); }
   * @Test public void test112() throws SQLException { execute( 112 ); }
   * @Test public void test113() throws SQLException { execute( 113 ); }
   * @Test public void test114() throws SQLException { execute( 114 ); }
   * @Test public void test115() throws SQLException { execute( 115 ); }
   * @Test public void test116() throws SQLException { execute( 116 ); }
   * @Test public void test117() throws SQLException { execute( 117 ); }
   * @Test public void test118() throws SQLException { execute( 118 ); }
   * @Test public void test119() throws SQLException { execute( 119 ); }
   * @Test public void test120() throws SQLException { execute( 120 ); }
   * @Test public void test121() throws SQLException { execute( 121 ); }
   * @Test public void test122() throws SQLException { execute( 122 ); }
   * @Test public void test123() throws SQLException { execute( 123 ); }
   * @Test public void test124() throws SQLException { execute( 124 ); }
   * @Test public void test125() throws SQLException { execute( 125 ); }
   * @Test public void test126() throws SQLException { execute( 126 ); }
   * @Test public void test127() throws SQLException { execute( 127 ); }
   * @Test public void test128() throws SQLException { execute( 128 ); }
   * @Test public void test129() throws SQLException { execute( 129 ); }
   * @Test public void test130() throws SQLException { execute( 130 ); }
   * @Test public void test131() throws SQLException { execute( 131 ); }
   * @Test public void test132() throws SQLException { execute( 132 ); }
   * @Test public void test133() throws SQLException { execute( 133 ); }
   * @Test public void test134() throws SQLException { execute( 134 ); }
   * @Test public void test135() throws SQLException { execute( 135 ); }
   * @Test public void test136() throws SQLException { execute( 136 ); }
   * @Test public void test137() throws SQLException { execute( 137 ); }
   * @Test public void test138() throws SQLException { execute( 138 ); }
   * @Test public void test139() throws SQLException { execute( 139 ); }
   * @Test public void test140() throws SQLException { execute( 140 ); }
   * @Test public void test141() throws SQLException { execute( 141 ); }
   * @Test public void test142() throws SQLException { execute( 142 ); }
   * @Test public void test143() throws SQLException { execute( 143 ); }
   * @Test public void test144() throws SQLException { execute( 144 ); }
   * @Test public void test145() throws SQLException { execute( 145 ); }
   * @Test public void test146() throws SQLException { execute( 146 ); }
   * @Test public void test147() throws SQLException { execute( 147 ); }
   * @Test public void test148() throws SQLException { execute( 148 ); }
   * @Test public void test149() throws SQLException { execute( 149 ); }
   * @Test public void test150() throws SQLException { execute( 150 ); }
   * @Test public void test151() throws SQLException { execute( 151 ); }
   * @Test public void test152() throws SQLException { execute( 152 ); }
   * @Test public void test153() throws SQLException { execute( 153 ); }
   * @Test public void test154() throws SQLException { execute( 154 ); }
   * @Test public void test155() throws SQLException { execute( 155 ); }
   * @Test public void test156() throws SQLException { execute( 156 ); }
   * @Test public void test157() throws SQLException { execute( 157 ); }
   * @Test public void test158() throws SQLException { execute( 158 ); }
   * @Test public void test159() throws SQLException { execute( 159 ); }
   * @Test public void test160() throws SQLException { execute( 160 ); }
   * @Test public void test161() throws SQLException { execute( 161 ); }
   * @Test public void test162() throws SQLException { execute( 162 ); }
   * @Test public void test163() throws SQLException { execute( 163 ); }
   * @Test public void test164() throws SQLException { execute( 164 ); }
   * @Test public void test165() throws SQLException { execute( 165 ); }
   * @Test public void test166() throws SQLException { execute( 166 ); }
   * @Test public void test167() throws SQLException { execute( 167 ); }
   * @Test public void test168() throws SQLException { execute( 168 ); }
   * @Test public void test169() throws SQLException { execute( 169 ); }
   * @Test public void test170() throws SQLException { execute( 170 ); }
   * @Test public void test171() throws SQLException { execute( 171 ); }
   * @Test public void test172() throws SQLException { execute( 172 ); }
   * @Test public void test173() throws SQLException { execute( 173 ); }
   * @Test public void test174() throws SQLException { execute( 174 ); }
   * @Test public void test175() throws SQLException { execute( 175 ); }
   * @Test public void test176() throws SQLException { execute( 176 ); }
   * @Test public void test177() throws SQLException { execute( 177 ); }
   * @Test public void test178() throws SQLException { execute( 178 ); }
   * @Test public void test179() throws SQLException { execute( 179 ); }
   * @Test public void test180() throws SQLException { execute( 180 ); }
   * @Test public void test181() throws SQLException { execute( 181 ); }
   * @Test public void test182() throws SQLException { execute( 182 ); }
   * @Test public void test183() throws SQLException { execute( 183 ); }
   * @Test public void test184() throws SQLException { execute( 184 ); }
   * @Test public void test185() throws SQLException { execute( 185 ); }
   * @Test public void test186() throws SQLException { execute( 186 ); }
   * @Test public void test187() throws SQLException { execute( 187 ); }
   * @Test public void test188() throws SQLException { execute( 188 ); }
   * @Test public void test189() throws SQLException { execute( 189 ); }
   * @Test public void test190() throws SQLException { execute( 190 ); }
   * @Test public void test191() throws SQLException { execute( 191 ); }
   * @Test public void test192() throws SQLException { execute( 192 ); }
   * @Test public void test193() throws SQLException { execute( 193 ); }
   * @Test public void test194() throws SQLException { execute( 194 ); }
   * @Test public void test195() throws SQLException { execute( 195 ); }
   * @Test public void test196() throws SQLException { execute( 196 ); }
   * @Test public void test197() throws SQLException { execute( 197 ); }
   * @Test public void test198() throws SQLException { execute( 198 ); }
   * @Test public void test199() throws SQLException { execute( 199 ); }
   * @Test public void test200() throws SQLException { execute( 200 ); }
   * @Test public void test201() throws SQLException { execute( 201 ); }
   * @Test public void test202() throws SQLException { execute( 202 ); }
   * @Test public void test203() throws SQLException { execute( 203 ); }
   * @Test public void test204() throws SQLException { execute( 204 ); }
   * @Test public void test205() throws SQLException { execute( 205 ); }
   * @Test public void test206() throws SQLException { execute( 206 ); }
   * @Test public void test207() throws SQLException { execute( 207 ); }
   * @Test public void test208() throws SQLException { execute( 208 ); }
   * @Test public void test209() throws SQLException { execute( 209 ); }
   * @Test public void test210() throws SQLException { execute( 210 ); }
   * @Test public void test211() throws SQLException { execute( 211 ); }
   * @Test public void test212() throws SQLException { execute( 212 ); }
   * @Test public void test213() throws SQLException { execute( 213 ); }
   * @Test public void test214() throws SQLException { execute( 214 ); }
   * @Test public void test215() throws SQLException { execute( 215 ); }
   * @Test public void test216() throws SQLException { execute( 216 ); }
   * @Test public void test217() throws SQLException { execute( 217 ); }
   * @Test public void test218() throws SQLException { execute( 218 ); }
   * @Test public void test219() throws SQLException { execute( 219 ); }
   * @Test public void test220() throws SQLException { execute( 220 ); }
   * @Test public void test221() throws SQLException { execute( 221 ); }
   * @Test public void test222() throws SQLException { execute( 222 ); }
   * @Test public void test223() throws SQLException { execute( 223 ); }
   * @Test public void test224() throws SQLException { execute( 224 ); }
   * @Test public void test225() throws SQLException { execute( 225 ); }
   * @Test public void test226() throws SQLException { execute( 226 ); }
   * @Test public void test227() throws SQLException { execute( 227 ); }
   * @Test public void test228() throws SQLException { execute( 228 ); }
   * @Test public void test229() throws SQLException { execute( 229 ); }
   * @Test public void test230() throws SQLException { execute( 230 ); }
   * @Test public void test231() throws SQLException { execute( 231 ); }
   * @Test public void test232() throws SQLException { execute( 232 ); }
   * @Test public void test233() throws SQLException { execute( 233 ); }
   * @Test public void test234() throws SQLException { execute( 234 ); }
   * @Test public void test235() throws SQLException { execute( 235 ); }
   * @Test public void test236() throws SQLException { execute( 236 ); }
   * @Test public void test237() throws SQLException { execute( 237 ); }
   * @Test public void test238() throws SQLException { execute( 238 ); }
   * @Test public void test239() throws SQLException { execute( 239 ); }
   * @Test public void test240() throws SQLException { execute( 240 ); }
   * @Test public void test241() throws SQLException { execute( 241 ); }
   * @Test public void test242() throws SQLException { execute( 242 ); }
   * @Test public void test243() throws SQLException { execute( 243 ); }
   * @Test public void test244() throws SQLException { execute( 244 ); }
   * @Test public void test245() throws SQLException { execute( 245 ); }
   * @Test public void test246() throws SQLException { execute( 246 ); }
   * @Test public void test247() throws SQLException { execute( 247 ); }
   * @Test public void test248() throws SQLException { execute( 248 ); }
   * @Test public void test249() throws SQLException { execute( 249 ); }
   * @Test public void test250() throws SQLException { execute( 250 ); }
   * @Test public void test251() throws SQLException { execute( 251 ); }
   * @Test public void test252() throws SQLException { execute( 252 ); }
   * @Test public void test253() throws SQLException { execute( 253 ); }
   * @Test public void test254() throws SQLException { execute( 254 ); }
   * @Test public void test255() throws SQLException { execute( 255 ); }
   * @Test public void test256() throws SQLException { execute( 256 ); }
   * @Test public void test257() throws SQLException { execute( 257 ); }
   * @Test public void test258() throws SQLException { execute( 258 ); }
   * @Test public void test259() throws SQLException { execute( 259 ); }
   * @Test public void test260() throws SQLException { execute( 260 ); }
   * @Test public void test261() throws SQLException { execute( 261 ); }
   * @Test public void test262() throws SQLException { execute( 262 ); }
   * @Test public void test263() throws SQLException { execute( 263 ); }
   * @Test public void test264() throws SQLException { execute( 264 ); }
   * @Test public void test265() throws SQLException { execute( 265 ); }
   * @Test public void test266() throws SQLException { execute( 266 ); }
   * @Test public void test267() throws SQLException { execute( 267 ); }
   * @Test public void test268() throws SQLException { execute( 268 ); }
   * @Test public void test269() throws SQLException { execute( 269 ); }
   * @Test public void test270() throws SQLException { execute( 270 ); }
   * @Test public void test271() throws SQLException { execute( 271 ); }
   * @Test public void test272() throws SQLException { execute( 272 ); }
   * @Test public void test273() throws SQLException { execute( 273 ); }
   * @Test public void test274() throws SQLException { execute( 274 ); }
   * @Test public void test275() throws SQLException { execute( 275 ); }
   * @Test public void test276() throws SQLException { execute( 276 ); }
   * @Test public void test277() throws SQLException { execute( 277 ); }
   * @Test public void test278() throws SQLException { execute( 278 ); }
   * @Test public void test279() throws SQLException { execute( 279 ); }
   * @Test public void test280() throws SQLException { execute( 280 ); }
   * @Test public void test281() throws SQLException { execute( 281 ); }
   * @Test public void test282() throws SQLException { execute( 282 ); }
   * @Test public void test283() throws SQLException { execute( 283 ); }
   * @Test public void test284() throws SQLException { execute( 284 ); }
   * @Test public void test285() throws SQLException { execute( 285 ); }
   * @Test public void test286() throws SQLException { execute( 286 ); }
   * @Test public void test287() throws SQLException { execute( 287 ); }
   * @Test public void test288() throws SQLException { execute( 288 ); }
   * @Test public void test289() throws SQLException { execute( 289 ); }
   * @Test public void test290() throws SQLException { execute( 290 ); }
   * @Test public void test291() throws SQLException { execute( 291 ); }
   * @Test public void test292() throws SQLException { execute( 292 ); }
   * @Test public void test293() throws SQLException { execute( 293 ); }
   * @Test public void test294() throws SQLException { execute( 294 ); }
   * @Test public void test295() throws SQLException { execute( 295 ); }
   * @Test public void test296() throws SQLException { execute( 296 ); }
   * @Test public void test297() throws SQLException { execute( 297 ); }
   * @Test public void test298() throws SQLException { execute( 298 ); }
   * @Test public void test299() throws SQLException { execute( 299 ); }
   */

  private void importData( String idVista, int idTheaterDBS ) throws SQLException
  {
    Date ini = new Date();
    TheaterTO theater = (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( this.theaterDAO
        .findByIdVistaAndActive( idVista ).get( 0 ) );
    Connection conn = this.getConnectionDBS();

    if( theater == null )
    {
      System.err.println( "Theater not found: " + idVista );
      return;
    }

//    if( theater.getRegion().getCatalogRegion().getId().intValue() != 3 )
//    {
//      System.err.println( "No es zona norte: " + theater.getRegion().getCatalogRegion().getName() );
//      return;
//    }

    System.out.println( "--------------------------" );
    System.out.println( ini );
    System.out.println( "Importing data: " + theater.getName() + ", id: " + theater.getId() );

    PreparedStatement ps = conn.prepareStatement( getBookings() );
    ps.setInt( 1, idTheaterDBS );
    ResultSet rs = ps.executeQuery();

    List<WeekDO> weeks = this.weekDAO.findAll();

    int i = 0;
    int n = 0;
    while( rs.next() )
    {

      if( !this.getEntityManager().getTransaction().isActive() )
      {
        this.getEntityManager().getTransaction().begin();
      }
      String codeDBS = rs.getString( "FilmID" );
      EventTO event = getEvent( codeDBS );
      Date playDateWeek = rs.getDate( "PlaydateWeek" );
      WeekDO week = getWeek( playDateWeek, weeks );
      if( week == null )
      {
        System.err.println( "No se encontró playDateWeek: " + playDateWeek );
      }

      int nuWeek = rs.getInt( "WeekNumber" );
      int nuScreen = rs.getInt( "Screen" );

      BookingDO bookingDO = getBooking( event, theater );
      BookingWeekDO bookingWeekDO = getBookingWeekDO( bookingDO, week, nuWeek );
      bookingWeekDO.setQtCopy( bookingWeekDO.getBookingWeekScreenDOList().size() + 1 );

      ScreenDO screen = getScreen( theater, nuScreen );
      if( screen != null )
      {
        BookingWeekScreenDO bws = new BookingWeekScreenDO();
        if( rs.getString( "FinalWeek" ).equals( "Yes" ) )
        {
          bws.setIdBookingStatus( new BookingStatusDO( BookingStatus.TERMINATED.getId() ) );
        }
        else
        {
          bws.setIdBookingStatus( new BookingStatusDO( BookingStatus.BOOKED.getId() ) );
        }
        bws.setIdBookingWeek( bookingWeekDO );
        bws.setIdScreen( screen );

        this.bookingWeekScreenDAO.create( bws );
        this.bookingWeekScreenDAO.flush();
        bookingWeekDO.getBookingWeekScreenDOList().add( bws );
      }
      this.bookingWeekDAO.edit( bookingWeekDO );
      n++;
      if( ++i < 40 )
      {
        System.out.print( "." );
      }
      else
      {
        System.out.println( ". " + n + " registries in K_BOOKING_WEEK_SCREEN" );
        i = 0;
        this.getEntityManager().getTransaction().commit();
      }
    }
    if( this.getEntityManager().getTransaction().isActive() )
    {
      this.getEntityManager().getTransaction().commit();
    }
    System.out.println( ". " + n + " registries in K_BOOKING_WEEK_SCREEN" );
    Date end = new Date();
    System.out.println( end );
    System.out.println( "Importing data took " + (end.getTime() - ini.getTime()) + " ms" );
    System.out.println( "--------------------------" );
  }

  private ScreenDO getScreen( TheaterTO theater, int nuScreen )
  {
    ScreenDO screenDO = null;
    for( ScreenTO screen : theater.getScreens() )
    {
      if( screen.getNuScreen().equals( nuScreen ) )
      {
        screenDO = new ScreenDO( screen.getId().intValue() );
        break;
      }
    }
    return screenDO;
  }

  private BookingWeekDO getBookingWeekDO( BookingDO bookingDO, WeekDO week, int nuWeek )
  {
    BookingWeekDO bookingWeekDO = null;

    if( CollectionUtils.isNotEmpty( bookingDO.getBookingWeekDOList() ) )
    {
      for( BookingWeekDO bw : bookingDO.getBookingWeekDOList() )
      {
        if( bw.getIdWeek().equals( week ) )
        {
          bookingWeekDO = bw;
          break;
        }
      }
    }
    else
    {
      bookingDO.setBookingWeekDOList( new ArrayList<BookingWeekDO>() );
    }

    if( bookingWeekDO == null )
    {
      bookingWeekDO = new BookingWeekDO();
      bookingWeekDO.setBookingWeekScreenDOList( new ArrayList<BookingWeekScreenDO>() );
      bookingWeekDO.setDtExhibitionEndDate( null );
      bookingWeekDO.setDtLastModification( new Date() );
      bookingWeekDO.setFgActive( true );
      bookingWeekDO.setFgSend( true );
      bookingWeekDO.setIdBooking( bookingDO );
      bookingWeekDO.setIdBookingStatus( new BookingStatusDO( BookingStatus.BOOKED.getId() ) );
      bookingWeekDO.setIdLastUserModifier( 1 );
      bookingWeekDO.setIdWeek( week );
      bookingWeekDO.setNuExhibitionWeek( nuWeek );
      bookingWeekDO.setQtCopy( 0 );

      bookingDO.getBookingWeekDOList().add( bookingWeekDO );

      this.bookingWeekDAO.create( bookingWeekDO );
      this.bookingDAO.edit( bookingDO );
      this.bookingWeekDAO.flush();
    }

    return bookingWeekDO;
  }

  private BookingDO getBooking( EventTO event, TheaterTO theater )
  {
    BookingDO bookingDO = this.bookingDAO.findByEventIdAndTheaterId( theater.getId(), event.getIdEvent() );
    if( bookingDO == null )
    {
      bookingDO = new BookingDO();
      bookingDO.setIdLastUserModifier( 1 );
      bookingDO.setDtLastModification( new Date() );
      bookingDO.setFgActive( true );
      bookingDO.setBookingWeekDOList( new ArrayList<BookingWeekDO>() );
      bookingDO.setIdEvent( new EventDO( event.getIdEvent() ) );
      bookingDO.setIdTheater( new TheaterDO( theater.getId().intValue() ) );
      bookingDO.setFgBooked( false );
      this.bookingDAO.create( bookingDO );
      this.bookingDAO.flush();
    }

    return bookingDO;
  }

  private WeekDO getWeek( Date playDateWeek, List<WeekDO> weeks )
  {
    WeekDO weekDO = null;
    for( WeekDO week : weeks )
    {
      if( week.getDtStartingDayWeek().getTime() == playDateWeek.getTime() )
      {
        weekDO = week;
        break;
      }
    }
    return weekDO;
  }

  private EventTO getEvent( String codeDBS )
  {
    return this.eventDAO.findByDsCodeDbs( codeDBS ).get( 0 );
  }

  private void saveBooking( BookingTO bookingTO )
  {
    if( bookingTO != null )
    {
      this.bookingDAO.save( bookingTO );
    }
  }

  private String getBookings()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( " select t.TheaterName, t.TheaterCode, tts.FilmID, tts.FilmID FilmCode, tp.Title, tp.ShortTitle, tts.DateAdded, tts.PlaydateWeek, tts.startDate, tts.Screen, tts.WeekNumber, coalesce(tts.FinalWeek, 'No') as FinalWeek  " );
    sb.append( " from tblDBSTheaterScreens tts " );
    sb.append( " inner join tblTheaters t  on tts.TheaterID = t.TheaterID " );
    sb.append( " inner join tblProjects tp on tp.ProjectID = tts.FilmID  " );
    sb.append( " where 1 = 1 " );
    sb.append( " and tts.PlaydateWeek >= '2012-01-01'" );
    sb.append( " and tts.startDate >= '2014-06-01'  " );
    sb.append( " and tts.startDate <= '2014-07-26'  " );
    sb.append( " and tts.TheaterID = ? " );
    sb.append( " and tts.Accepted != -2" );
    sb.append( " and tts.Screen != 0  " );
    sb.append( " order by tts.DateAdded, tts.FilmID, tts.startDate, tts.Screen " );

    return sb.toString();

  }

  private Connection getConnectionDBS() throws SQLException
  {

    Connection conn = null;
    Properties connectionProps = new Properties();
//    connectionProps.put( "user", "sa" );
//    connectionProps.put( "password", "edtdcci77" );
//    conn = DriverManager.getConnection( "jdbc:jtds:sqlserver://10.2.70.91:1433;databaseName=DBS-MX", connectionProps );
     connectionProps.put( "user", "DBS-MX" );
     connectionProps.put( "password", "jmdrrc" );
     conn = DriverManager.getConnection( "jdbc:jtds:sqlserver://10.20.20.31:1433;databaseName=DBS-MX", connectionProps
     );

    System.out.println( "Connected to database DBS-MX" );
    return conn;
  }

}
