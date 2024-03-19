package com.telcel.gsa.dsaf.entity;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;



//@Entity
@Table(name = "v_bajas_nvo3")
public class v_bajas_nvo3 implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8770161601016048115L;
	private String region;
	private String regionDesc;
	private Integer perbaja;
	private String clase;
	private String descClase;
	private String texto;
	private String denom;
	private String numActivo;
	private String sub;
	private Integer dInv;
	private Integer areaVal;
	private Integer dia;
	private Integer mes;
	private Integer anio;
	private Integer anioTd;
	private Integer mesTd;
	private Integer perBaja;
	private Double adqBaja;
	private Double adqAcBaja;
	private Double ejercicioBaja;
    private Double deprTot;
    private Double costoH;
    private Double inpcMp;
    private Double inpc;
    private Double facAct;
    private Double costoAct;
    private Double depreAnualAct;
	
	

	public v_bajas_nvo3() {
	}

	public v_bajas_nvo3(String region, String regionDesc, Integer perbaja,
	String clase,
	String descClase,
	String texto,
	String denom,
	String numActivo,
	String sub,
	Integer dInv, Integer areaVal,
	Integer dia,
	Integer mes,
	Integer anio, Integer anioTd,
	Integer mesTd,
	Integer perBaja,Double adqBaja,
	Double adqAcBaja,
	Double ejercicioBaja,
    Double deprTot,
    Double costoH,
    Double inpcMp,
    Double inpc, Double facAct,
    Double costoAct,
    Double depreAnualAct) {
		this.region = region;
		this.regionDesc =regionDesc;
		this.perbaja = perbaja;
		this.clase = clase;
		this.descClase= descClase;
		this.texto= texto;
		this.denom = denom;
		this.numActivo = numActivo;
		this.sub= sub;
		this.dInv = dInv;
		this.areaVal = areaVal;
		this.dia= dia;
		this.mes= mes;
		this.anio = anio;
		this.anioTd= anioTd;
		this.mesTd= mesTd;
		this.perBaja= perBaja;
		this.adqBaja = adqBaja;
		this.adqAcBaja= adqAcBaja;
		this.ejercicioBaja= ejercicioBaja;
		this.deprTot= deprTot;
		this.costoH= costoH;
		this.inpcMp= inpcMp;
		this.inpc = inpc;
		this.facAct= facAct;
		this.costoAct = costoAct;
		this.depreAnualAct= depreAnualAct;
		
		
	
	}

	public v_bajas_nvo3(String region, String regionDesc) {
		this.region = region;
		this.regionDesc = regionDesc;
		
	}
	
	
	
	
	@Column(name = "region", nullable = false, length = 50)
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "region_dsc", nullable = false, length = 50)
	public String getRegionDesc() {
		return regionDesc;
	}

	public void setRegionDesc(String regionDesc) {
		this.regionDesc = regionDesc;
	}
	
	@Column(name = "per_baja", nullable = false, length = 50)
	public Integer getPerbaja() {
		return perbaja;
	}
	
	public void setPerbaja(Integer perbaja) {
		this.perbaja = perbaja;
	}

	
	@Column(name = "clase", nullable = false, length = 10)
	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}
	@Column(name = "clase_dsc", nullable = false, length = 50)
	public String getDescClase() {
		return descClase;
	}

	public void setDescClase(String descClase) {
		this.descClase = descClase;
	}
	@Column(name = "texto_baja", nullable = false, length = 40)
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	@Column(name = "denom", nullable = false, length = 100)
	public String getDenom() {
		return denom;
	}

	public void setDenom(String denom) {
		this.denom = denom;
	}

	@Column(name = "num_activo", nullable = false, length = 12)
	public String getNumActivo() {
		return numActivo;
	}

	public void setNumActivo(String numActivo) {
		this.numActivo = numActivo;
	}
	
	@Column(name = "sub", nullable = false, length = 4)
	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}
	
	@Column(name = "d_inv", nullable = false, length = 2)
	public Integer getdInv() {
		return dInv;
	}

	public void setdInv(Integer dInv) {
		this.dInv = dInv;
	}
	
	@Column(name = "area_val", nullable = false, length = 2)
	public Integer getAreaVal() {
		return areaVal;
	}

	public void setAreaVal(Integer areaVal) {
		this.areaVal = areaVal;
	}

	@Column(name = "dia", nullable = false, length = 2)
	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	@Column(name = "mes", nullable = false, length = 2)
	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	@Column(name = "anio", nullable = false, length = 5)
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@Column(name = "anio_td", nullable = false, length = 5)
	public Integer getAnioTd() {
		return anioTd;
	}

	public void setAnioTd(Integer anioTd) {
		this.anioTd = anioTd;
	}

	@Column(name = "mes_td", nullable = false, length = 2)
	public Integer getMesTd() {
		return mesTd;
	}

	public void setMesTd(Integer mesTd) {
		this.mesTd = mesTd;
	}

	@Column(name = "per_baja", nullable = false)
	public Integer getPerBaja() {
		return perBaja;
	}

	public void setPerBaja(Integer perBaja) {
		this.perBaja = perBaja;
	}
	
	@Column(name = "adq_baja", nullable = false)
	public Double getAdqBaja() {
		return adqBaja;
	}

	public void setAdqBaja(Double adqBaja) {
		this.adqBaja = adqBaja;
	}
	
	@Column(name = "adq_ac_baja", nullable = false)
	public Double getAdqAcBaja() {
		return adqAcBaja;
	}

	public void setAdqAcBaja(Double adqAcBaja) {
		this.adqAcBaja = adqAcBaja;
	}

	@Column(name = "ejercicio_baja", nullable = false)
	public Double getEjercicioBaja() {
		return ejercicioBaja;
	}

	public void setEjercicioBaja(Double ejercicioBaja) {
		this.ejercicioBaja = ejercicioBaja;
	}

	@Column(name = "depr_tot", nullable = false)
	public Double getDeprTot() {
		return deprTot;
	}

	public void setDeprTot(Double deprTot) {
		this.deprTot = deprTot;
	}
	
	@Column(name = "costo_h", nullable = false)
	public Double getCostoH() {
		return costoH;
	}

	public void setCostoH(Double costoH) {
		this.costoH = costoH;
	}
	
	@Column(name = "inpc_mp", nullable = false)
	public Double getInpcMp() {
		return inpcMp;
	}

	public void setInpcMp(Double inpcMp) {
		this.inpcMp = inpcMp;
	}

	@Column(name = "inpc", nullable = false)
	public Double getInpc() {
		return inpc;
	}

	public void setInpc(Double inpc) {
		this.inpc = inpc;
	}

	@Column(name = "fac_act", nullable = false)
	public Double getFacAct() {
		return facAct;
	}

	public void setFacAct(Double facAct) {
		this.facAct = facAct;
	}

	@Column(name = "costo_act", nullable = false)
	public Double getCostoAct() {
		return costoAct;
	}

	public void setCostoAct(Double costoAct) {
		this.costoAct = costoAct;
	}
	
	@Column(name = "depre_anual_act", nullable = false)
	public Double getDepreAnualAct() {
		return depreAnualAct;
	}

	public void setDepreAnualAct(Double depreAnualAct) {
		this.depreAnualAct = depreAnualAct;
	}

	

	

}
