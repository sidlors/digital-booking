package mx.com.cinepolis.digital.booking.commons.to;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * Transfer object for files
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class FileTO extends CatalogTO
{
  private static final long serialVersionUID = 4874339035171107294L;
  private byte[] file;

  /**
   * @return the file
   */
  public byte[] getFile()
  {
    return CinepolisUtils.enhacedByteArrayClone( file );
  }

  /**
   * @param file the file to set
   */
  public void setFile( byte[] file )
  {
    this.file = CinepolisUtils.enhacedByteArrayClone( file );
  }

}
