package gov.nih.nci.cadsr.dao.model;

/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class AcRegistrationsModel extends BaseModel
{
    private String acIdseq;
    private String arIdseq;
    private String registrationStatus;
    private String regisIdseq;

    public AcRegistrationsModel()
    {
    }

    public String getAcIdseq()
    {
        return acIdseq;
    }

    public void setAcIdseq( String acIdseq )
    {
        this.acIdseq = acIdseq;
    }

    public String getArIdseq()
    {
        return arIdseq;
    }

    public void setArIdseq( String arIdseq )
    {
        this.arIdseq = arIdseq;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public String getRegisIdseq()
    {
        return regisIdseq;
    }

    public void setRegisIdseq( String regisIdseq )
    {
        this.regisIdseq = regisIdseq;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof AcRegistrationsModel ) ) return false;

        AcRegistrationsModel that = ( AcRegistrationsModel ) o;

        if( getAcIdseq() != null ? !getAcIdseq().equals( that.getAcIdseq() ) : that.getAcIdseq() != null ) return false;
        if( getArIdseq() != null ? !getArIdseq().equals( that.getArIdseq() ) : that.getArIdseq() != null ) return false;
        if( getRegistrationStatus() != null ? !getRegistrationStatus().equals( that.getRegistrationStatus() ) : that.getRegistrationStatus() != null )
            return false;
        return !( getRegisIdseq() != null ? !getRegisIdseq().equals( that.getRegisIdseq() ) : that.getRegisIdseq() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getAcIdseq() != null ? getAcIdseq().hashCode() : 0;
        result = 31 * result + ( getArIdseq() != null ? getArIdseq().hashCode() : 0 );
        result = 31 * result + ( getRegistrationStatus() != null ? getRegistrationStatus().hashCode() : 0 );
        result = 31 * result + ( getRegisIdseq() != null ? getRegisIdseq().hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "AcRegistrationsModel{" +
                "acIdseq='" + acIdseq + '\'' +
                ", arIdseq='" + arIdseq + '\'' +
                ", registrationStatus='" + registrationStatus + '\'' +
                ", regisIdseq='" + regisIdseq + '\'' +
                '}';
    }
}
