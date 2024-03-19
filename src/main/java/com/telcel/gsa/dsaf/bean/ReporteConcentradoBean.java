package com.telcel.gsa.dsaf.bean;
// Generated 12/02/2020 05:14:21 PM by Hibernate Tools 3.5.0.Final

import java.util.List;



public class ReporteConcentradoBean implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7902420255405837671L;
	private String datSoc;
	private boolean acum;
	
	private String[] meses;
	private String[] mesesselect;
	private List<Integer> anios;
	private Integer anio;
	private String strAnio;
	
	private BajasCalculoBean calculo;
	private List<BajasCalculoBean> calculos;
	private List<String> region;
	private List<CatRegionBean> regiones;
	private List<String> clase;
	private List<BajasClaseBean> clases;
	private String texto;
	private List<String> textos;
	private List<String> txtDesc;
	private List<String> txtsDesc;
	private boolean descarga;
	
	private Integer dInv;
	private Integer areaVal;
	private String mesesConsulta;
	private List<BajasDosBean> listReporte;
	
	// listas generales
	
	private TotalBajasDosBean totReporteGeneral;
	private List<BajasDosBean> listTotalReporteGeneralTot;
	
	private List<BajasDosBean> listTotalGlobalReg;
	private List<TGRegionBean> detRegiones;
	
	//Listas de ajustes
	private List<BajasDosBean> listTotalGlobalAjReg;
	private TotalBajasDosBean totReporteAjGeneral;
	//TOTALES
	private TotalBajasDosBean totalGlobalRegTot;
	
	private List<TotalBajasDosBean> listTotalGlobalAjRegTot;

	private TotalBean totReporteMes;
	private TotalBean totReporteMesGeneral;
	private TotalBean totAjusteMes;
	
	private List<ReporteBean> listReporteAjuste;

	private TotalBean totReporteMesAjuste;
	private TotalBajasDosBean totReporteMesAjusteGeneral;
	
	private TotalBajasDosBean totReporteMesAjusteGran;
	private List<TotalBajasDosBean> totReporteMesAjusteGranLst;
	
	private BajasTxtLbl txtLbl;
	private List<BajasTxtLbl> txtLbls;
	private BajasAjustesBean ajuste;
	private List<BajasAjustesBean> ajustes;
	private boolean regDisabled;
	private boolean claseDisabled;
	private boolean txtDisabled;
	private boolean queryDisabled;
	
	//reporte pdf y excel
	private String calculotxt;
	private String mesReptxt;
	private String claseReptxt;
	private String regionReptxt;
	private String tituloReporte;
	private String textosTitulos;
	

	private String titReptxt;//titulo que le coloca en el reporte excel region, clase, texto
	private String anioRepCorto;
	
	private String ruta;
	private String mesSeleccion;
	
	
	
	
	public String getDatSoc() {
		return datSoc;
	}
	public void setDatSoc(String datSoc) {
		this.datSoc = datSoc;
	}
	public String getTextosTitulos() {
		return textosTitulos;
	}
	public void setTextosTitulos(String textosTitulos) {
		this.textosTitulos = textosTitulos;
	}
	public String getMesSeleccion() {
		return mesSeleccion;
	}
	public void setMesSeleccion(String mesSeleccion) {
		this.mesSeleccion = mesSeleccion;
	}
	public String getTituloReporte() {
		return tituloReporte;
	}
	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}

	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public TotalBajasDosBean getTotReporteMesAjusteGran() {
		return totReporteMesAjusteGran;
	}
	public void setTotReporteMesAjusteGran(TotalBajasDosBean totReporteMesAjusteGran) {
		this.totReporteMesAjusteGran = totReporteMesAjusteGran;
	}
	public TotalBean getTotReporteMesGeneral() {
		return totReporteMesGeneral;
	}
	public void setTotReporteMesGeneral(TotalBean totReporteMesGeneral) {
		this.totReporteMesGeneral = totReporteMesGeneral;
	}
	public TotalBajasDosBean getTotReporteMesAjusteGeneral() {
		return totReporteMesAjusteGeneral;
	}
	public void setTotReporteMesAjusteGeneral(TotalBajasDosBean totReporteMesAjusteGeneral) {
		this.totReporteMesAjusteGeneral = totReporteMesAjusteGeneral;
	}
	public TotalBajasDosBean getTotReporteGeneral() {
		return totReporteGeneral;
	}
	public void setTotReporteGeneral(TotalBajasDosBean totReporteGeneral) {
		this.totReporteGeneral = totReporteGeneral;
	}
	public boolean isAcum() {
		return acum;
	}
	public void setAcum(boolean acum) {
		this.acum = acum;
	}
	public String[] getMeses() {
		return meses;
	}
	public void setMeses(String[] meses) {
		this.meses = meses;
	}
	public String[] getMesesselect() {
		return mesesselect;
	}
	public void setMesesselect(String[] mesesselect) {
		this.mesesselect = mesesselect;
	}
	public List<Integer> getAnios() {
		return anios;
	}
	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public BajasCalculoBean getCalculo() {
		return calculo;
	}
	public void setCalculo(BajasCalculoBean calculo) {
		this.calculo = calculo;
	}
	public List<BajasCalculoBean> getCalculos() {
		return calculos;
	}
	public void setCalculos(List<BajasCalculoBean> calculos) {
		this.calculos = calculos;
	}
	public List<String> getRegion() {
		return region;
	}
	public void setRegion(List<String> region) {
		this.region = region;
	}
	public List<CatRegionBean> getRegiones() {
		return regiones;
	}
	public void setRegiones(List<CatRegionBean> regiones) {
		this.regiones = regiones;
	}
	public List<String> getClase() {
		return clase;
	}
	public void setClase(List<String> clase) {
		this.clase = clase;
	}
	public List<BajasClaseBean> getClases() {
		return clases;
	}
	public void setClases(List<BajasClaseBean> clases) {
		this.clases = clases;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public List<String> getTextos() {
		return textos;
	}
	public void setTextos(List<String> textos) {
		this.textos = textos;
	}
	public List<String> getTxtDesc() {
		return txtDesc;
	}
	public void setTxtDesc(List<String> txtDesc) {
		this.txtDesc = txtDesc;
	}
	public List<String> getTxtsDesc() {
		return txtsDesc;
	}
	public void setTxtsDesc(List<String> txtsDesc) {
		this.txtsDesc = txtsDesc;
	}
	public boolean isDescarga() {
		return descarga;
	}
	public void setDescarga(boolean descarga) {
		this.descarga = descarga;
	}
	public Integer getdInv() {
		return dInv;
	}
	public void setdInv(Integer dInv) {
		this.dInv = dInv;
	}
	public Integer getAreaVal() {
		return areaVal;
	}
	public void setAreaVal(Integer areaVal) {
		this.areaVal = areaVal;
	}
	public String getMesesConsulta() {
		return mesesConsulta;
	}
	public void setMesesConsulta(String mesesConsulta) {
		this.mesesConsulta = mesesConsulta;
	}
	
	public List<BajasDosBean> getListReporte() {
		return listReporte;
	}
	public void setListReporte(List<BajasDosBean> listReporte) {
		this.listReporte = listReporte;
	}
	public List<ReporteBean> getListReporteAjuste() {
		return listReporteAjuste;
	}
	public void setListReporteAjuste(List<ReporteBean> listReporteAjuste) {
		this.listReporteAjuste = listReporteAjuste;
	}

	public BajasTxtLbl getTxtLbl() {
		return txtLbl;
	}
	public void setTxtLbl(BajasTxtLbl txtLbl) {
		this.txtLbl = txtLbl;
	}
	public List<BajasTxtLbl> getTxtLbls() {
		return txtLbls;
	}
	public void setTxtLbls(List<BajasTxtLbl> txtLbls) {
		this.txtLbls = txtLbls;
	}
	public BajasAjustesBean getAjuste() {
		return ajuste;
	}
	public void setAjuste(BajasAjustesBean ajuste) {
		this.ajuste = ajuste;
	}
	public List<BajasAjustesBean> getAjustes() {
		return ajustes;
	}
	public void setAjustes(List<BajasAjustesBean> ajustes) {
		this.ajustes = ajustes;
	}
	public boolean isRegDisabled() {
		return regDisabled;
	}
	public void setRegDisabled(boolean regDisabled) {
		this.regDisabled = regDisabled;
	}
	public boolean isClaseDisabled() {
		return claseDisabled;
	}
	public void setClaseDisabled(boolean claseDisabled) {
		this.claseDisabled = claseDisabled;
	}
	public boolean isTxtDisabled() {
		return txtDisabled;
	}
	public void setTxtDisabled(boolean txtDisabled) {
		this.txtDisabled = txtDisabled;
	}
	public boolean isQueryDisabled() {
		return queryDisabled;
	}
	public void setQueryDisabled(boolean queryDisabled) {
		this.queryDisabled = queryDisabled;
	}
	public String getCalculotxt() {
		return calculotxt;
	}
	public void setCalculotxt(String calculotxt) {
		this.calculotxt = calculotxt;
	}
	
	public String getMesReptxt() {
		return mesReptxt;
	}
	public void setMesReptxt(String mesReptxt) {
		this.mesReptxt = mesReptxt;
	}
	public String getClaseReptxt() {
		return claseReptxt;
	}
	public void setClaseReptxt(String claseReptxt) {
		this.claseReptxt = claseReptxt;
	}
	public String getRegionReptxt() {
		return regionReptxt;
	}
	public void setRegionReptxt(String regionReptxt) {
		this.regionReptxt = regionReptxt;
	}
	
	public String getTitReptxt() {
		return titReptxt;
	}
	public void setTitReptxt(String titReptxt) {
		this.titReptxt = titReptxt;
	}
	public String getAnioRepCorto() {
		return anioRepCorto;
	}
	public void setAnioRepCorto(String anioRepCorto) {
		this.anioRepCorto = anioRepCorto;
	}
	public String getStrAnio() {
		return strAnio;
	}
	public void setStrAnio(String strAnio) {
		this.strAnio = strAnio;
	}
	public TotalBean getTotReporteMes() {
		return totReporteMes;
	}
	public void setTotReporteMes(TotalBean totReporteMes) {
		this.totReporteMes = totReporteMes;
	}
	public TotalBean getTotAjusteMes() {
		return totAjusteMes;
	}
	public void setTotAjusteMes(TotalBean totAjusteMes) {
		this.totAjusteMes = totAjusteMes;
	}
	public TotalBean getTotReporteMesAjuste() {
		return totReporteMesAjuste;
	}
	public void setTotReporteMesAjuste(TotalBean totReporteMesAjuste) {
		this.totReporteMesAjuste = totReporteMesAjuste;
	}
	
	public List<TotalBajasDosBean> getTotReporteMesAjusteGranLst() {
		return totReporteMesAjusteGranLst;
	}
	public void setTotReporteMesAjusteGranLst(List<TotalBajasDosBean> totReporteMesAjusteGranLst) {
		this.totReporteMesAjusteGranLst = totReporteMesAjusteGranLst;
	}
	public List<BajasDosBean> getListTotalGlobalReg() {
		return listTotalGlobalReg;
	}
	public void setListTotalGlobalReg(List<BajasDosBean> listTotalGlobalReg) {
		this.listTotalGlobalReg = listTotalGlobalReg;
	}
	
	public List<BajasDosBean> getListTotalReporteGeneralTot() {
		return listTotalReporteGeneralTot;
	}
	public void setListTotalReporteGeneralTot(List<BajasDosBean> listTotalReporteGeneralTot) {
		this.listTotalReporteGeneralTot = listTotalReporteGeneralTot;
	}
	public List<BajasDosBean> getListTotalGlobalAjReg() {
		return listTotalGlobalAjReg;
	}
	public void setListTotalGlobalAjReg(List<BajasDosBean> listTotalGlobalAjReg) {
		this.listTotalGlobalAjReg = listTotalGlobalAjReg;
	}
	
	public List<TGRegionBean> getDetRegiones() {
		return detRegiones;
	}
	public void setDetRegiones(List<TGRegionBean> detRegiones) {
		this.detRegiones = detRegiones;
	}
	
	public TotalBajasDosBean getTotReporteAjGeneral() {
		return totReporteAjGeneral;
	}
	public void setTotReporteAjGeneral(TotalBajasDosBean totReporteAjGeneral) {
		this.totReporteAjGeneral = totReporteAjGeneral;
	}
	public List<TotalBajasDosBean> getListTotalGlobalAjRegTot() {
		return listTotalGlobalAjRegTot;
	}
	public void setListTotalGlobalAjRegTot(List<TotalBajasDosBean> listTotalGlobalAjRegTot) {
		this.listTotalGlobalAjRegTot = listTotalGlobalAjRegTot;
	}
	public TotalBajasDosBean getTotalGlobalRegTot() {
		return totalGlobalRegTot;
	}
	public void setTotalGlobalRegTot(TotalBajasDosBean totalGlobalRegTot) {
		this.totalGlobalRegTot = totalGlobalRegTot;
	}
	
	
	
	
	}
	
	
	
	
	
	
	


	
	
	


