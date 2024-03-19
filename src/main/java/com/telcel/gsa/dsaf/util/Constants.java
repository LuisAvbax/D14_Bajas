package com.telcel.gsa.dsaf.util;

public final class Constants {
    
	 public static final String ERROR_SISTEMA = "Se detecto un error en el sistema";
	 //GENRICOS
	 public static final String COMMA=",";
	 public static final boolean ESTATUS_TRUE=true;
	 public static final boolean ESTATUS_FALSE=false;
	 
	 public static final String APP_ID="D14_Bajas: ";
	 	
	 //Autenticacion
	 public static final String LOGIN_LDAP_ENDPOINT="LDAP_ENDPOINT";
	 public static final String LOGIN_LDAP_TNS="LDAP_TNS";
	 public static final String LOGIN_LDAP_WSNAME="LDAP_WSNAME";
	 public static final String LOGIN_LDAP_ERROR="Error de conexión a WSDL de servicio web";
	 public static final String LOGIN_LDAP_ACTIVE="LDAP_ACTIVE";
	 public static final String LOGIN_LDAP_ID_APP="LDAP_ID_APP";
	 public static final String LOGIN_LDAP_BASE_URL="LDAP_BASE_URL";
	 public static final String FILTRO_TEXTOS="TIPO_TEXTO";
	 public static final String LOGIN_LDAP_CLAVE_APP="LDAP_CLAVE_APP";
	 public static final String LOGIN_LDAP_HOST="LDAP_HOST";
	 public static final String LOGIN_LDAP_PORT="LDAP_PORT";
	 public static final String LOGIN_DATA_MONTHS="DATA_MONTHS";
	 public static final String LOGIN_DATA_YEARS_BACK="DATA_YEARS_BACK";
	 

	 //CARGA
	 public static final String CARGA_MES_ANTERIOR= "CARGA_MES_ANTERIOR";
	 public static final String CARGA_COD_DATOSINCOMPLETOS="Datos incompletos";
	 public static final String CARGA_COD_DATOSFALTANTES="Datos faltantes";
	 public static final String CARGA_COD_PROYECTONOEXISTE="No proyecto";
	 public static final String CARGA_COD_TARIFANOEXISTE="No tarifa";
	 public static final String CARGA_COD_DIVISIONNOEXISTE="No division";
	 public static final String CARGA_COD_REGIONNOEXISTE="No region";
	 public static final String CARGA_COD_SITIOEXISTENTE="Existe nombre";
	 public static final String CARGA_COD_ESTADONOEXISTE="No estado";
	 public static final String CARGA_COD_NOCC="No cc";
	 public static final String CARGA_COD_CCDUPLICADO="Existe cc";
	 
	 public static final String CARGA_MSG_DATOSINCOMPLETOS="DATOS INCOMPLETOS PARA EL RPU INDICADO";
	 public static final String CARGA_MSG_PROYECTONOEXISTE="EL PROYECTO NO SE ENCONTRÓ EN EL CATÁLOGO";
	 public static final String CARGA_MSG_TARIFANOEXISTE="LA TARIFA NO SE ENCONTRÓ EN EL CATÁLOGO";
	 public static final String CARGA_MSG_DIVISIONNOEXISTE="LA DIVISIÓN NO SE ENCONTRÓ EN EL CATÁLOGO";
	 public static final String CARGA_MSG_REGIONNOEXISTE="LA REGIÓN NO SE ENCONTRÓ EN EL CATÁLOGO";
	 public static final String CARGA_MSG_SITIOEXISTENTE="EL NOMBRE O EL RPU DEL SITIO YA EXISTE";
	 public static final String CARGA_MSG_ESTADONOEXISTE="LA POBLACIÓN NO SE ENCONTRÓ EN EL CATÁLOGO";
	 public static final String CARGA_MSG_CCNOEXISTE="EL CENTRO DE COSTOS NO SE ENCONTRÓ EN EL CATÁLOGO";
	 public static final String CARGA_MSG_CCDUPLICADO="CENTRO DE COSTOS DUPLICADO PARA EL RPU INDICADO";
	 public static final String BITACORA_CODIGO_EXCEDETAMDESC="UNO DE LOS CAMPOS EXCEDE EL TAMAÑO MAXIMO PERMITIDO";
	 public static final String CARGA_CODIGO_ESPACIO="\\u00A0";
	 public static final String CARGA_VACIO="";
	 public static final String CARGA_CONSUMO="CONSUMO";
	 public static final String CARGA_TEXTO="CARGA";
	 
	 //BAJO FACTOR PORTENCIA
	 public static final String PARAM_FACTORPOTENCIA="DATA_BAJO_FACTOR_POTENCIA";
	 
	 //FLOWSCOPE
	 public static final String FLOWSCOPE_VALIDACION="validacion";
	 public static final String FLOWSCOPE_USER="user";
	 
	 
	 //MAIL
	 public static final String MAIL_STANDBY="STANDBY";
	 public static final String MAIL_SEND="SEND";
	 public static final String MAIL_FAIL="MAIL_FAIL"; 
	 	
	 //ESTATUS SITIO
	 public static final String SITIO_EST_CONCLUIDO="CONCLUIDO";
	 public static final String CADENA_VACIA="";
	//Bitacora
	 public static final String BITACORA_CODIGO_NOEXISTE="ORA-02291";
	 public static final String BITACORA_CODIGO_YAEXISTE="ORA-00001";
	 public static final String BITACORA_CODIGO_EXCEDETAM="ORA-12899";
	 public static final String BITACORA_VACIO="VACIO";
	 public static final String BITACORA_NA="N/A";
	 public static final String BITACORA_CONSUMO="CONSUMO";
	 public static final String BITACORA_CARGA="CARGA";
	 public static final String BITACORA_ERROR="ERROR";
	 public static final String BITACORA_EXITO="EXITO";
	 public static final String BITACORA_SITIO="SITIO";
	 public static final String BITACORA_ALTA="ALTA";
	 public static final String BITACORA_BAJA="BAJA";
	 public static final String BITACORA_BAJAFIN="BAJAFIN";
	 public static final String BITACORA_WARNING="ADVERTENCIA";
	 public static final String BITACORA_CORRIJA_ERROR="Corriga los errores presentados en bitacora antes de registrar los consumos";
	 public static final String BITACORA_ERROR_CARGA_CON="Ocurrió un error al cargar los consumos, verifique bitacora para mas detalle";
	 public static final String BITACORA_ERROR_BAJA="Ocurrió un error realizar la baja, verifique bitacora para mas detalle";
	 public static final String BITACORA_ERROR_BAJAFIN="Ocurrió un error realizar la baja finiquito, verifique bitacora para mas detalle";
	 public static final String BITACORA_BAJA_EXITO="Baja(s) realizada(s) con exito!";
	 public static final String BITACORA_BAJAFIN_EXITO="Baja(s) finiquito realizada(s) con exito!";
	 public static final String BITACORA_CARGA_EXITO="Carga realizada con exito!";
	 public static final String BITACORA_ERROR_CONTSITIO="Se encontraron errores en el contenido del archivo, verifique la bitacora";
	 public static final String BITACORA_OK_VALCONSUMO=" correcto. Presione registrar para cargar los consumos";
	 public static final String BITACORA_RPU_INEXISTENTE="EL RPU indicado no se encuentra registrado en sistema Hoja: ";
	 public static final String BITACORA_RPU_FALTANDATOS="Datos incompletos para el RPU indicado. Hoja: ";
	 public static final String BITACORA_RPU_YAREGISTRADO="El RPU ya se encuentra registrado. Hoja: ";
	 public static final String BITACORA_DATOS_FALTANTES="Datos faltantes en seccion de consumos";
	 public static final String BITACORA_CONSUMO_REGISTRADO="EL CONSUMO YA SE ENCUENTRA REGISTRADO. HOJA: ";
	 public static final String BITACORA_MSG_DATOS_FALTANTES="DATOS FALTANTES EN LA SECCION DE CONSUMOS. HOJA: ";
	 public static final String BITACORA_MSG_FORMATONC="EL FORMATO DE LOS CAMPOS NO CORRESPONDE. HOJA: ";
	 public static final String BITACORA_MSG_EXCEDETAM="EXISTE UN CAMPO DEMASIADO GRANDE PARA CARGAR EN SISTEMA. HOJA: ";
	 public static final String BITACORA_MSG_LINEAI=" LINEA(";
	 public static final String BITACORA_MSG_LINEAF=")";
	 
	 //REPORTES
	 public static final String REP_RESUMENR_PARAMFECHA="paramFecha";
	 public static final String REP_RESUMENR_PARAMANIO="paramAnio";
	 public static final String REP_RESUMENR_TEMPLATE="resumenRegion.jasper";
	 public static final String REP_RESUMENR_CONTENTDISP="Content-disposition";
	 public static final String REP_RESUMENR_FILENAME="attachment; filename=ResumenRegion.xls";
	 public static final String REP_POLIZASAP_FILENAME="attachment; filename=PolizaSAP";
	 public static final String REP_FACTORPOTENCIA_FILENAME="attachment; filename=FactorPotencia";
	 public static final String REP_CONSUMOS_FILENAME="attachment; filename=Consumos";
	 public static final String REP_CONSUMOS_TOTAL_FILENAME="attachment; filename=ConsumosTotal";
	 public static final String REP_SITIOS_FILENAME="attachment; filename=Sitios";
	 public static final String REP_RESUMENREGION_FILENAME="attachment; filename=ResumenRegion";
	 public static final String REP_RESUMENREGIONC_FILENAME="attachment; filename=ResumenContable";
	 public static final String REP_BITACORA_FILENAME="attachment; filename=Bitacora";
	 public static final String REP_ADQ_REG_FILENAME="attachment; filename=AqdBajaRegion";
	 public static final String REP_ADQ_CLAS_FILENAME="attachment; filename=AqdBajaClase";
	 public static final String REP_ADQ_TEXTO_FILENAME="attachment; filename=AqdBajaTexto";
	 public static final String REP_DPR_REG_FILENAME="attachment; filename=DprActRegion";
	 public static final String REP_DPR_CLAS_FILENAME="attachment; filename=DprActClase";
	 public static final String REP_DPR_TEXTO_FILENAME="attachment; filename=DprActTexto";
	 public static final String REP_COST_REG_FILENAME="attachment; filename=CostBajaRegion";
	 public static final String REP_COST_CLAS_FILENAME="attachment; filename=CostBajaClase";
	 public static final String REP_COST_TEXTO_FILENAME="attachment; filename=CostBajaTexto";
	 public static final String REP_T_GREG_FILENAME="attachment; filename=TotGlobalRegion";
	 public static final String REP_RES_CON_REG_FILENAME="attachment; filename=ResumenConpRegion";
	 public static final String REP_RES_DET_AJ_TPO_FILENAME="attachment; filename=DetalladoAjustePorTipo";
	 public static final String REP_RES_CON_CLAS_FILENAME="attachment; filename=ResumenConpClase";
	 public static final String REP_T_GCLA_FILENAME="attachment; filename=TotGlobalClase";
	 public static final String REP_INSTREG_FILENAME="attachment; filename=InstitucionalRegion";
	 public static final String REP_INSTCLA_FILENAME="attachment; filename=InstitucionalClase";
	 public static final String REP_RES_CONCENTRADO_FILENAME="attachment; filename=ReporteConcentrado";
	 public static final String REP_RES_DETALLADO_FILENAME="attachment; filename=ReporteDetallado";
	 public static final String REP_RES_DETALLADO_AJUSTE_FILENAME="attachment; filename=ReporteDetalladoAjuste";
	 public static final String REP_RESUSMEN_AJUSTE_FILENAME="attachment; filename=ResumenAjuste";
	 public static final String REP_INSTAJUSTE_FILENAMEXLS="attachment; filename=ResumenInstitucionalAjuste";
	 public static final String REP_INSTAJUSTE_FILENAME="attachment; filename=ResumenInstitucionalAjusteR3-53";
	 public static final String REP_INSTAJUSTE_FILENAMER0="attachment; filename=ResumenInstitucionalAjusteR0-2";
	 public static final String REP_INSTAJUSTE_FILENAMER4a6="attachment; filename=ResumenInstitucionalAjusteR6-8";
	 public static final String REP_INSTAJUSTE_FILENAMER7a9="attachment; filename=ResumenInstitucionalAjusteR9";
	 public static final String REP_EXCELD2003_EXT=".xls";
	 public static final String REP_EXCELU2003_EXT=".xlsx";
	 public static final String REP_EXCELCSV_EXT=".csv";
	 public static final String FORMATOFECHA = "dd/MM/yyyy";
	 public static final String FORMATOFECHAPUNTO = "dd.MM.yyyy";
	 public static final String FORMATOFECHAHORA = "dd/MM/yyyy HH:mm";
	 public static final String FORMATOANIO = "yyyy";
	 public static final String FORMATOMILES = "#,##0.00####";
	 
	 //GRAFICOS
	 public static final String SININFO = "SIN INFORMACION";
	 public static final String GRAFP = "grafp";
	 public static final String GRAF = "graf";
	 public static final String TITMONTOSTFA = "Montos por tarifa";
	 public static final String TITSITIOSTFA = "Sitios por tarifa";
	 
	 
	 //Valores para la pantalla de Alta de planes
	 public static final String CAMPOERROR="campoError";
	 public static final String CARGAR_ARCHIVO="cargarArchivo";
	 public static final String CARGA_ARCHIVO="cargaArchivo";
	 public static final String ACEPTARARCHIVO="aceptarArchivo";
	 public static final String SELECCIONARARCHIVO="seleccionarArchivo";
	 public static final String DESCRIPCION_TIPO_PLAN_DATOS= "DATOS";
	 public static final String PLAN_COST_CONTROL = "CostControl";
	 public static final String PLAN_KIT_A_CREDITO= "KIT A CREDITO";
	 public static final String PLAN_MIXTO= "MIXTO";
	 public static final String PLAN_BASICO= "BASICO";
	 public static final String MASXMENOS= "Mas x Menos";
	 public static final String MASXMENOSLOCAL ="Mas x Menos-LOCAL";
	 public static final String LIST_REGISTROSPLANES= "listaDeRegistrosPlan";
	 public static final String REGION_1="region1";
	 public static final String REGION_2="region2";
	 public static final String REGION_3="region3";
	 public static final String REGION_4="region4";
	 public static final String REGION_5="region5";
	 public static final String REGION_6="region6";
	 public static final String REGION_7="region7";
	 public static final String REGION_8="region8";
	 public static final String REGION_9="region9";
	 public static final String ALL_REGION="todasLasRegiones";
	    
	 public static final String PLAZO_LIBRE= "plazoLibre";
	 public static final String PLAZO_6MESES= "6M";
	 public static final String PLAZO_12MESES= "12M";
	 public static final String PLAZO_18MESES= "18M";
	 public static final String PLAZO_24MESES= "24M";
	 public static final String PLAZO_36MESES= "36M";
	 public static final String ALL_PLAZOS= "allPlazos";
	 public static final String PLAZO_OTROS= "";

	 public static final String P_L= "LIBRE";
	 public static final String SEISM ="6 MESES";
	 public static final String DOCEM="12 MESES";
	 public static final String DIESIOCHOM="18 MESES";
	 public static final String VEINTICUATROM="24 MESES";
	 public static final String TRENTAYSEISM="36 MESES";
	 public static final String ALL="";
	    
	 public static final String R1="1";
	 public static final String R2="2";
	 public static final String R3="3";
	 public static final String R4="4";
	 public static final String R5="5";
	 public static final String R6="6";
	 public static final String R7="7";
	 public static final String R8="8";
	 public static final String R9="9";	   

	 public static final String CHARSET = "ISO-8859-1";
	 //NUMEROS
	 
	 public static final String S_UNO= "1";
	 
	 
	 public static final int tamanioRPU=15;
	 public static final int tamanioKWH=10;
	 public static final int tamanioDemanda=10;
	 public static final int tamanioReactivos=10;
	 public static final int tamanioFp=10;
	 public static final int tamanioFc=10;
	 public static final int tamanioEnergia=19;
	 public static final int tamanioDap=19;
	 public static final int tamanioIva=19;
	 public static final int tamanioOtros=19;
	 public static final int tamanioTotal=19;
	 
	 
	 public static final int CERO=0;
	 public static final int UNO= 1; 
	 public static final int DOS= 2; 
	 public static final int TRES= 3; 
	 public static final int CUATRO= 4; 
	 public static final int CINCO= 5; 
	 public static final int SEIS= 6; 
	 public static final int SIETE= 7; 
	 public static final int OCHO= 8; 
	 public static final int NUEVE= 9;
	 public static final int DIEZ=10;
	 public static final int ONCE=11;
	 public static final int DOCE=12;
	 public static final int TRECE=13;
	 public static final int CATORCE=14;
	 public static final int QUINCE=15;
	 public static final int DIESISEIS=16;
	 public static final int DIESISIETE=17;
	 public static final int DIESIOCHO=18;
	 public static final int DIESINUEVE=19;
	 public static final int VEINTE=20;
	 public static final int VEINTIUNO=21;
	 public static final int VEINTIDOS=22;
	 public static final int VEINTITRES=23;
	 public static final int VEINTICUATRO=24;
	 public static final int VEINTICINCO=25;
	 public static final int VEINTISEIS=26;
	 public static final int VEINTISIETE=27;
	 public static final int VEINTIOCHO=28;
	 public static final int VEINTINUEVE=29;
	 public static final int TREINTA=30;
	 public static final int DOS_CINCO= 25; 	 
	 public static final int DOS_SEIS = 26; 
	 public static final int DOS_SIETE = 27; 
	 public static final int DOS_OCHO = 28; 
	 public static final int DOS_NUEVE = 29; 
	 public static final int TRES_CERO = 30; 
	 public static final int TRES_UNO = 31; 
	 public static final int TRES_DOS = 32; 
	 
	 public static final int NOVENTAYNUEVE= 99; 
	 
	 public static final int DIEZ_CERO_CERO= 1000;
	 
	 public static final int VEINTE_TREINTA= 2030; 
	 
	 public static final int TREINTA_VEINTE= 3020; 
	 public static final int TREINTA_TREINTA= 3030; 

	 public static final int CUARENTA_DIEZ= 4010; 
	 public static final int SESENTA_NOVENTA= 6090; 

	 public static final int SETENTA_CERO_CERO= 7000; 
	 public static final int SETENTA_DIEZ= 7010; 

	 public static final int OCHENTA_CERO_CERO= 8000; 
	 public static final int OCHENTA_DIEZ= 8010; 
	 public static final int OCHENTA_VEINTE= 8020; 

	 
	 public static final long TRES_CUATRO_L= 34; 

	 
	 public static final long VEINTE_DIEZ_L= 2010; 
	 public static final long VEINTE_VEINTE_L= 2020; 
	 public static final long VEINTE_TREINTA_L= 2030; 
	 
	 public static final long TREINTA_DIEZ_L= 3010; 
	 public static final long TREINTA_VEINTE_L= 3020; 
	 public static final long TREINTA_TREINTA_L= 3030; 

	 
	 public static final long CINCUENTA_DIEZ_L= 5010; 
	 public static final long CINCUENTA_VEINTE_L= 5020; 

	 public static final long SESENTA_DIEZ_L= 6010; 
	 public static final long SESENTA_VEINTE_L= 6020; 
	 public static final long SESENTA_OCHENTA_L= 6080; 
	 public static final long SESENTA_NOVENTA_L= 6090; 

	 
	 public static final long SETENTA_DIEZ_L= 7010;
	 
	 public static final long OCHENTA_TREINTA_L= 8030; 
	 public static final long OCHENTA_CINCUENTA_L= 8050; 
	 
	 

	 
		
	 public static final String CAMPO_VACIO="CAMPO_VACIO";
	 public static final String REGIONES_OBLIGATORIAS="REGIONES_OBLIGATORIAS";
	 public static final String ERROR_EXCEL="ERROR_EXCEL";
	 public static final String PLAZOS_OBLIGATORIOS="PLAZOS_OBLIGATORIOS";
	 public static final String SEGMENTO_OBLIGATORIO="SEGMENTO_OBLIGATORIO";
	 public static final String TIPO_PLAN_OBLIGATORIO="TIPO_PLAN_OBLIGATORIO";
	 public static final String CARACTERES_EXTRANOS="CARACTERES_EXTRANOS";
	 public static final String PERFIL_EXISTENTE="PERFIL_EXISTENTE";
     public static final String NUMERICO="NUMERICO";
	 public static final String CORREO="CORREO";
	 public static final String REGISTRO_EXISTENTE = "REGISTRO_EXISTENTE";
	 public static final String REGION_EXISTENTE = "REGION_EXISTENTE";
	 public static final String MENSAJE_DESCRIPCION_REPETIDO="DESCRIPCION_REPETIDA";
	 public static final String MENSAJE_REQUERIO ="campoRequerido";
	 public static final String BAD_FORMAT_CATA ="BAD_FORMAT_CATA";
	 public static final String BAD_FORMAT_PROD ="BAD_FORMAT_PROD";
	 
	 
	 public static final String ID_ADMIN_CATALOGOS ="idAdminCatalogos";
	 public static final String ADMIN_CATALOGOS = "catalogos";
	 
	 public static final Long ESTADO_INICIAL = 1l;
	 public static final Long TIPO_SOLICITUD = 1l;
	 
	 public static final String CATALOGO_XREF="catalogoXREF";
	 public static final String BUSCAR_CATALOGO_XREF="buscarCatalogoXREF";
	 public static final String LIST_CATALOGO_XREF= "listaXREF";
	 
	 public static final String DEFAULTCOMBOVALUES= "-1";
	 
	 public static final String SPLIT_PIPE ="\\|";
	 public static final String CERO_INTEGER_LONG="0";
	 public static final int DEFAULT_ID_CATA = 205;
	 public static final int VALOR_0 =0;
	 public static final int VALOR_1 =1;
	 public static final int VALOR_2 =2;
	 public static final int VALOR_3 =3;
	 public static final int VALOR_4 =4;
	 public static final int VALOR_5 =5;
	 public static final int VALOR_6 =6;
	 public static final int VALOR_7 =7;
	 public static final int VALOR_8 =8;
	 public static final int VALOR_9 =9;
	 public static final int VALOR_99 =99;
	 
	 //Plazos
	 
	 public static final String PLAZO_0="00";
	 public static final String PLAZO_6="06";
	 public static final String PLAZO_12="12";
	 public static final String PLAZO_18="18";
	 public static final String PLAZO_24="24";
	 public static final String PLAZO_36="36";
	 public static final String LIBRE="L";
	 public static final String FORZOSO="F";
	 
	
	
	
	public static final String RESUMEN = "resumen";
	public static final String LISTA_BITACORA = "listBitacora";
	public static final String REGION_SELECCIONADA = "regionSeleccionada";
	public static final String LISTA_CVESPT_NO_EXISTEN_BD = "listaCvesPlanTarifNoExisten";
	public static final String REPORTE_REGLAS_NEGOCIO = "reporteReglaNegocio";
	public static final String LISTA_ARCHIVOS = "listaArchivos";
	public static final String SOLO_DESCARGA = "soloDescarga";
	public static final String DESHABLITA_GROWL = "deshabiltaGrowl";
	public static final String TIPO_REPORTE = "tipoReporte";
	public static final String REPORTE_LAYOUT_CARGA = "Reporte Carga";
	public static final String REPORTE_LAYOUT_MODIFICACION = "Reporte Modificacion";
	

	//Envio correo
	public static final String REMITENTE = "control_altaplanes@mail.telcel.com";
	public static final String IP_CORREO="10.191.143.163";
	public static final String PUERTO_CORREO="25";
	
	public static final String ROLE_IMPL="IMPL";
	public static final String ROLE_CXP="CXP";
	public static final String ROLE_REGIONAL="REGIONAL";
	public static final String ROLE_COORD="COORD";
	
	public static final String SMTP_FROM="SMTP_FROM";
	public static final String MAIL_CXP="MAIL_CXP";
	public static final String MAIL_REG="MAIL_REG";	
	public static final String MAIL_CXP_CC="MAIL_CXP";
	public static final String MAIL_REG_CC="MAIL_REG";
	public static final String MAIL_CXP_BCC="MAIL_CXP";
	public static final String MAIL_REG_BCC="MAIL_REG";
	public static final String MAIL_CXP_SUBJECT="MAIL_CXP_SUBJECT";
	public static final String MAIL_REG_SUBJECT="MAIL_REG_SUBJECT";
	public static final String MAIL_SYSTEM="MAIL_SYSTEM";
	
	public static final String MAIL_BAJA = "MAIL_BAJA";
	public static final String MAIL_BAJA_BCC = "MAIL_BAJA_BCC";
	public static final String MAIL_BAJA_CC = "MAIL_BAJA_CC";
	public static final String MAIL_BAJA_SUBJECT = "MAIL_BAJA_SUBJECT";
	public static final String MAIL_BAJA_TEXT = "MAIL_BAJA_TEXT";
	 
	public static final String BAJA_NUMERO_DIAS_NOTF = "BAJA_NUMERO_DIAS_NOTF";
	public static final String BAJA_NUMERO_DIAS_TOT = "BAJA_NUMERO_DIAS_TOT";

	 public static final int REGLA_PRODUCTO = 3;
	
   
   //
   public static final int PLANTARIFARIO = 0;
   public static final int DESCRIPCIONCORTADELPLAN = 1;
   public static final int DESCRIPCIONLARGADELPLAN = 2;
   public static final int PLANMPP = 3;
   public static final int COSTODELARENTA = 4;
   public static final int ANALOGICODIGITAL = 5;
   public static final int LIBREOFORZOSO = 6;
   public static final int GENERAIVA = 7;
   public static final int PLANESCORPORATIVO = 8;
   public static final int PLANTARIFAUNICA = 9;
   public static final int PLAZOFORZOSO = 10;
   public static final int MONITOREOPORLCREDIT = 11;
   public static final int CATEGORIAENFACTURACION = 12;
   public static final int TIPODECUENTA = 13;
   public static final int CLASEDECREDITO = 14;
   public static final int FORMADEPAGO = 15;
   public static final int MONTOPORLIMITEDECREDITO = 16;
   public static final int CARGOPORCANCELACION0 = 17;
   public static final int CARGOPORCANCELACION1 = 18;
   public static final int CARGOPORCANCELACION2 = 19;
   public static final int CARGOPORCANCELACION3 = 20;
   public static final int CLASESDECREDITOPARALU = 21;
   public static final int MONTOPARALIMITEDEUSO = 22;
   public static final int FIANZA = 23;
   public static final int PRODUCTO_NUM = 24;
   public static final int RENTAINCLUIDAPLANTARIFARIO = 33;
   public static final int TAIRECANTIDAD = 34;
   public static final int TACCGCANTIDAD = 35;
   public static final int TACC2CANTIDAD = 36;
   public static final int TACCSCANTIDAD = 37;
   public static final int TAIRECOSTO = 34;
   public static final int TACCGCOSTO = 35;
   public static final int TACC2COSTO = 36;
   public static final int TACCSCOSTO = 37;
   public static final int TAIRECODIGO = 34;
   public static final int TACCGCODIGO = 35;
   public static final int TACC2CODIGO = 36;
   public static final int TACCSCODIGO = 37;
   public static final int TIPOPLANTARIFARIO = 38;
   public static final int TIPOCLIENTE = 39;
   public static final int CAMBIOSIGUIENTECICLO = 40;
   public static final int REGULADOTARIFA = 41;
   public static final int COBROMIXTSCOLLAMADAS = 42;
   public static final int TASACIONPORVOLUMEN = 43;
   public static final int APLICAANUEVOSUSUARIOS = 44;
   public static final int PLANDEDESCUENTO = 45;
   public static final int UNIDADDETASACION = 46;
   public static final int PRORRATEALARENTA = 47;
   public static final int FACTURAADELANTADARENTA = 48;
   public static final int MONTOPARALIMITEDECREDITO = 49;
   public static final int TIPODEPLAN = 50;
   public static final int MOSINCLUIDOS = 51;
   public static final int AJUSTEKITACREDITO = 52;
   public static final int DEPOSITODEADENDUM = 53;
   public static final int DEPOSITODECONSUMO = 54;
   public static final int CREDITOVENTANILLA = 55;
   public static final int CREDITOBANCO = 56;
   public static final int APLICAEQUIPOILIMITADO = 57;
   public static final int TACC1CANTIDAD = 58;
   public static final int TATTSCANTIDAD = 59;
   public static final int TATTECANTIDAD = 60;
   public static final int TATXSCANTIDAD = 61;
   public static final int TATXECANTIDAD = 62;
   public static final int TAOPSCANTIDAD = 63;
   public static final int TAOPECANTIDAD = 64;
   public static final int TACXSCANTIDAD = 65;
   public static final int TACFSCANTIDAD = 66;
   public static final int TACC1COSTO = 58;
   public static final int TATTSCOSTO = 59;
   public static final int TATTECOSTO = 60;
   public static final int TATXSCOSTO = 61;
   public static final int TATXECOSTO = 62;
   public static final int TAOPSCOSTO = 63;
   public static final int TAOPECOSTO = 64;
   public static final int TACXSCOSTO = 65;
   public static final int TACFSCOSTO = 66;
   public static final int TACC1CODIGO = 58;
   public static final int TATTSCODIGO = 59;
   public static final int TATTECODIGO = 60;
   public static final int TATXSCODIGO = 61;
   public static final int TATXECODIGO = 62;
   public static final int TAOPSCODIGO = 63;
   public static final int TAOPECODIGO = 64;
   public static final int TACXSCODIGO = 65;
   public static final int TACFSCODIGO = 66;
   public static final int PLANDEVOZODATOS = 67;
   public static final int PLANORIGEN = 68;
   public static final int GRUPODEPLAN = 69;
   public static final int PLANHOMONIMO = 70;
   public static final int DUENODELPLAN = 71;
   public static final int RESTRICCIONDEVOZYSMS = 72;
   public static final int IEPS = 73;
   public static final int CLASIFICACION = 74;
   public static final int DESCRIPCIONRENTA = 75;
   public static final int CATEGORIAENREPORTES = 76;
   public static final int CLASIFICACION2 = 77;
   public static final int FAMILIA_NUM = 78;
   public static final int PUNTOSCIRCULOAZUL = 79;
   public static final int TALI1CANTIDAD = 80;
   public static final int TALI2CANTIDAD = 81;
   public static final int TALI3CANTIDAD = 82;
   public static final int THLOSCANTIDAD = 83;
   public static final int THLTSCANTIDAD = 84;
   public static final int THLXSCANTIDAD = 85;
   public static final int TLLOSCANTIDAD = 86;
   public static final int TLLTSCANTIDAD = 87;
   public static final int TLLXSCANTIDAD = 88;
   public static final int TRLOSCANTIDAD = 89;
   public static final int TRLTSCANTIDAD = 90;
   public static final int TRLXSCANTIDAD = 91;
   public static final int TALI4CANTIDAD = 92;
   public static final int THNOSCANTIDAD = 93;
   public static final int THNTSCANTIDAD = 94;
   public static final int THNXSCANTIDAD = 95;
   public static final int TLNOSCANTIDAD = 96;
   public static final int TLNTSCANTIDAD = 97;
   public static final int TLNXSCANTIDAD = 98;
   public static final int TRNOSCANTIDAD = 99;
   public static final int TRNTSCANTIDAD = 100;
   public static final int TRNXSCANTIDAD = 101;
   public static final int TB03SCANTIDAD = 102;
   public static final int TB04SCANTIDAD = 103;
   public static final int TB06SCANTIDAD = 104;
   public static final int TGGGECANTIDAD = 105;
   public static final int TGLGECANTIDAD = 106;
   public static final int TGNGECANTIDAD = 107;
   public static final int TI07SCANTIDAD = 108;
   public static final int TI08SCANTIDAD = 109;
   public static final int LB02SCANTIDAD = 110;
   public static final int TALI1COSTO = 80;
   public static final int TALI2COSTO = 81;
   public static final int TALI3COSTO = 82;
   public static final int THLOSCOSTO = 83;
   public static final int THLTSCOSTO = 84;
   public static final int THLXSCOSTO = 85;
   public static final int TLLOSCOSTO = 86;
   public static final int TLLTSCOSTO = 87;
   public static final int TLLXSCOSTO = 88;
   public static final int TRLOSCOSTO = 89;
   public static final int TRLTSCOSTO = 90;
   public static final int TRLXSCOSTO = 91;
   public static final int TALI4COSTO = 92;
   public static final int THNOSCOSTO = 93;
   public static final int THNTSCOSTO = 94;
   public static final int THNXSCOSTO = 95;
   public static final int TLNOSCOSTO = 96;
   public static final int TLNTSCOSTO = 97;
   public static final int TLNXSCOSTO = 98;
   public static final int TRNOSCOSTO = 99;
   public static final int TRNTSCOSTO = 100;
   public static final int TRNXSCOSTO = 101;
   public static final int TB03SCOSTO = 102;
   public static final int TB04SCOSTO = 103;
   public static final int TB06SCOSTO = 104;
   public static final int TGGGECOSTO = 105;
   public static final int TGLGECOSTO = 106;
   public static final int TGNGECOSTO = 107;
   public static final int TI07SCOSTO = 108;
   public static final int TI08SCOSTO = 109;
   public static final int LB02SCOSTO = 110;
   public static final int TALI1CODIGO = 80;
   public static final int TALI2CODIGO = 81;
   public static final int TALI3CODIGO = 82;
   public static final int THLOSCODIGO = 83;
   public static final int THLTSCODIGO = 84;
   public static final int THLXSCODIGO = 85;
   public static final int TLLOSCODIGO = 86;
   public static final int TLLTSCODIGO = 87;
   public static final int TLLXSCODIGO = 88;
   public static final int TRLOSCODIGO = 89;
   public static final int TRLTSCODIGO = 90;
   public static final int TRLXSCODIGO = 91;
   public static final int TALI4CODIGO = 92;
   public static final int THNOSCODIGO = 93;
   public static final int THNTSCODIGO = 94;
   public static final int THNXSCODIGO = 95;
   public static final int TLNOSCODIGO = 96;
   public static final int TLNTSCODIGO = 97;
   public static final int TLNXSCODIGO = 98;
   public static final int TRNOSCODIGO = 99;
   public static final int TRNTSCODIGO = 100;
   public static final int TRNXSCODIGO = 101;
   public static final int TB03SCODIGO = 102;
   public static final int TB04SCODIGO = 103;
   public static final int TB06SCODIGO = 104;
   public static final int TGGGECODIGO = 105;
   public static final int TGLGECODIGO = 106;
   public static final int TGNGECODIGO = 107;
   public static final int TI07SCODIGO = 108;
   public static final int TI08SCODIGO = 109;
   public static final int LB02SCODIGO = 110;
   public static final int RESERVADO = 111;

   public static final int TVIRECANTIDAD = 112;
   public static final int TVCCSCANTIDAD = 113;
   public static final int TVCXSCANTIDAD = 114;
   public static final int TVTTSCANTIDAD = 115;
   public static final int TVTTECANTIDAD = 116;
   public static final int TVTXSCANTIDAD = 117;
   public static final int TVTXECANTIDAD = 118;
   public static final int TVOPSCANTIDAD = 119;
   public static final int TVOPECANTIDAD = 120;
   public static final int TVIROCANTIDAD = 121;
   public static final int TVRNECANTIDAD = 122;
   public static final int TVRTSCANTIDAD = 123;
   public static final int TVRXSCANTIDAD = 124;
   public static final int TVROSCANTIDAD = 125;
   public static final int TVIRECOSTO = 112;
   public static final int TVCCSCOSTO = 113;
   public static final int TVCXSCOSTO = 114;
   public static final int TVTTSCOSTO = 115;
   public static final int TVTTECOSTO = 116;
   public static final int TVTXSCOSTO = 117;
   public static final int TVTXECOSTO = 118;
   public static final int TVOPSCOSTO = 119;
   public static final int TVOPECOSTO = 120;
   public static final int TVIROCOSTO = 121;
   public static final int TVRNECOSTO = 122;
   public static final int TVRTSCOSTO = 123;
   public static final int TVRXSCOSTO = 124;
   public static final int TVROSCOSTO = 125;
   public static final int TVIRECODIGO = 112;
   public static final int TVCCSCODIGO = 113;
   public static final int TVCXSCODIGO = 114;
   public static final int TVTTSCODIGO = 115;
   public static final int TVTTECODIGO = 116;
   public static final int TVTXSCODIGO = 117;
   public static final int TVTXECODIGO = 118;
   public static final int TVOPSCODIGO = 119;
   public static final int TVOPECODIGO = 120;
   public static final int TVIROCODIGO = 121;
   public static final int TVRNECODIGO = 122;
   public static final int TVRTSCODIGO = 123;
   public static final int TVRXSCODIGO = 124;
   public static final int TVROSCODIGO = 125;
   public static final int RESERVADO1 = 126;
   public static final int RESERVADO2 = 127;
   public static final int SUBCTA0CANTIDAD = 128;
   public static final int OPERACION0 = 129;
   public static final int SUBCTA1CANTIDAD = 130;
   public static final int OPERACION1 = 131;
   public static final int SUBCTA2CANTIDAD = 132;
   public static final int OPERACION2 = 133;
   public static final int SUBCTA3CANTIDAD = 134;
   public static final int OPERACION3 = 135;
   public static final int SUBCTA4CANTIDAD = 136;
   public static final int OPERACION4 = 137;
   public static final int SUBCTA5CANTIDAD = 138;
   public static final int OPERACION5 = 139;
   public static final int SUBCTA6CANTIDAD = 140;
   public static final int OPERACION6 = 141;
   public static final int SUBCTA7CANTIDAD = 142;
   public static final int OPERACION7 = 143;
   public static final int SUBCTA8CANTIDAD = 144;
   public static final int OPERACION8 = 145;
   public static final int SUBCTA9CANTIDAD = 146;
   public static final int OPERACION9 = 147;
   public static final int SUBCTA10CANTIDAD = 148;
   public static final int OPERACION10 = 149;
   public static final int SUBCTA11CANTIDAD = 150;
   public static final int OPERACION11 = 151;
   public static final int SUBCTA12CANTIDAD = 152;
   public static final int OPERACION12 = 153;
   public static final int SUBCTA0COSTO = 128;
   public static final int SUBCTA1COSTO = 130;
   public static final int SUBCTA2COSTO = 132;
   public static final int SUBCTA3COSTO = 134;
   public static final int SUBCTA4COSTO = 136;
   public static final int SUBCTA5COSTO = 138;
   public static final int SUBCTA6COSTO = 140;
   public static final int SUBCTA7COSTO = 142;
   public static final int SUBCTA8COSTO = 144;
   public static final int SUBCTA9COSTO = 146;
   public static final int SUBCTA10COSTO = 148;
   public static final int SUBCTA11COSTO = 150;
   public static final int SUBCTA12COSTO = 152;
   public static final int SUBCTA0CODIGO = 128;
   public static final int SUBCTA1CODIGO = 130;
   public static final int SUBCTA2CODIGO = 132;
   public static final int SUBCTA3CODIGO = 134;
   public static final int SUBCTA4CODIGO = 136;
   public static final int SUBCTA5CODIGO = 138;
   public static final int SUBCTA6CODIGO = 140;
   public static final int SUBCTA7CODIGO = 142;
   public static final int SUBCTA8CODIGO = 144;
   public static final int SUBCTA9CODIGO = 146;
   public static final int SUBCTA10CODIGO = 148;
   public static final int SUBCTA11CODIGO = 150;
   public static final int SUBCTA12CODIGO = 152;
   public static final int ACUMULADOR03 = 154;
   public static final int OPERACIONACUM03 = 155;
   public static final int ACUMULADOR10 = 156;
   public static final int OPERACIONACUM10 = 157;
   public static final int ACUMULADOR11 = 158;
   public static final int OPERACIONACUM11 = 159;
   public static final int ACUMULADOR12 = 160;
   public static final int OPERACIONACUM12 = 161;
   public static final int MERCADOA3P = 162;
   public static final int DESCRIPCIONA3P = 163;
   public static final int LET01 = 164;
   public static final int LET51 = 165;
   public static final int MCPME = 166;
   public static final int MCPMI = 167;
   public static final int SMBKE = 168;
   public static final int SMBKS = 169;
   public static final int SMBKD = 170;
   public static final int PENALIZACION1 = 171;
   public static final int PENALIZACION2 = 172;
   public static final int PENALIZACION3 = 173;
   public static final int PENALIZACION4 = 174;
   public static final int PENALIZACION5 = 175;
   public static final int PENALIZACION6 = 176;
   public static final int PENALIZACION7 = 177;
   public static final int PENALIZACION8 = 178;
   public static final int PENALIZACION9 = 179;
   public static final int PENALIZACION10 = 180;
   public static final int PENALIZACION11 = 181;
   public static final int PENALIZACION12 = 182;
   public static final int PLAND = 183;
   public static final int RPTISV = 184;
   public static final int RPTIEQ = 185;
   public static final int GRPPLN = 186;
   public static final int CLASIFICACIONSERVICE = 187;
   public static final int CARGASIAPSAM = 188;

   /*Archivo Muerto*/
   
   /*Catalogo de etiquetas*/
   public static final String LIST_ETIQUETAS= "listaEtiquetas";
   public static final String  ETIQUETASBEAN = "etiquetasBean";
   /*Parametros para consumir servicio LDAP*/
                                                 
	public static String ID_APP = "ID_APP";
	public static String CLAVE_APP = "CLAVE_APP";
	public static String WEB_HOST = "WEB_HOST";
	public static String WEB_PORT = "WEB_PORT";
	public static String BASE_URL = "BASE_URL";
                
	/*Busqueda de empleado*/

	public static final String BUSQUEDAPERSONAL_USULIST = "listaUsuarios";
	public static final String BUSQUEDAPERSONAL_USUARIO = "filtroPersonal";
	public static final String LISTA_DETALLES= "listasDetallesBean";
	public static final String USUARIO= "usuario";
	/*Solicitud de etiquetas*/
	public static final String USUARIOBEAN="usuarioBean";
	public static final String EXTRAVIO="EXTRAVIO";
	public static final String DETERIORO="DETERIORO";
	public static final String SOLOETIQUETABEAN="soloEtiquetaBean";
	public static final String ADVERTENCIA="Advertencia";
	public static final String COMBOBEANS="comboBeans";
	public static final String LISTAETIQUETASINBOX="etInbox";
	public static final String LISTSOLETI="listSolEti";
	public static final String MSGMOD="mensajeModificacion";
	public static final String EST_SOL_ETI_PENDIENTE="PENDIENTE";
	public static final String EST_SOL_ETI_CANCELADA="CANCELADA";
	public static final String EST_SOL_ETI_ATENDIDA="ATENDIDA";
	public static final String SOLICITUD="solicitud";
	public static final String MAX_ETIQUETAS="MAX_ETIQUETAS";
	 public static final String TOTAL_REGISTROSSOL = "totalRegistrosSol";
   
	/*Hoja transferencia*/
	public static final String EST_ETQ_ALTA_HOJA_TRANS = "PENDIENTE";
   private Constants(){}

   
   public static final String REP_SOL_ALTA = "Solicitud de Alta de Planes";
   public static final String REP_SOL_NOMBRE = "Nombre:";
   public static final String REP_SOL_TELEFONO = "Teléfono:";
   public static final String REP_SOL_OBSERV = "Observaciones:";
   public static final String REP_SOL_FECH_ELAB = "Fech. Elab.:";
   public static final String REP_SOL_EXT= "EXT SC.: ";
   public static final String REP_SOL_FECH_TERM= "Fech. Term.: ";
   public static final String REP_SOL_FECH_EFEC= "Fecha Efectiva :";
   public static final String REP_SOL_FOLIO= "Folio:";
   public static final String REP_SOL_REGIONES= "REGIONES :";
   public static final String REP_SOL_R1= "R01";
   public static final String REP_SOL_R2= "R02";
   public static final String REP_SOL_R3= "R03";
   public static final String REP_SOL_R4= "R04";
   public static final String REP_SOL_R5= "R05";
   public static final String REP_SOL_R6= "R06";
   public static final String REP_SOL_R7= "R07";
   public static final String REP_SOL_R8= "R08";
   public static final String REP_SOL_R9= "R09";  
   public static final String DB23="DB23";
   
   
   //reporte
   public static String TIT_BAJAS_ACTIVO_FIJO ="BAJAS DE ACTIVO FIJO";
   public static String TIT_REP_BAJ_REGION = "ADQUISICION BAJA POR REGION";
   public static String TIT_REP_BAJ_CLASE = "ADQUISICION BAJA POR CLASE";
   public static String TIT_REP_BAJ_TEXTO = "ADQUISICION BAJA POR TEXTO";
   public static String TIT_REP_DPR_REGION = "DEPRECIACION AÑO ACTUALIZADO POR REGION";
   public static String TIT_REP_DPR_CLASE = "DEPRECIACION AÑO ACTUALIZADO POR CLASE";
   public static String TIT_REP_DPR_TEXTO = "DEPRECIACION AÑO ACTUALIZADO POR TEXTO";
   public static String TIT_REP_COSTO_REGION = "COSTO ACTUALIZADO POR REGION";
   public static String TIT_REP_COSTO_CLASE = "COSTO ACTUALIZADO POR CLASE";
   public static String TIT_REP_COSTO_TEXTO = "COSTO ACTUALIZADO POR TEXTO";
   public static String TIT_REP_TOT_GLOB_REGION = "Total Global Por Región";
   public static String TIT_REP_TOT_GLOB_CLASE = "TOTAL GLOBAL POR CLASE";
   public static String TIT_REP_INST_REGION = "INSTITUCIONAL POR REGIÓN";
   public static String TIT_REP_INST_CLASE = "INSTITUCIONAL POR CLASE";
   public static String TIT_REP_INST_AJUSTES = "RESUMEN INSITUCIONAL DE AJUSTES";
   public static String TIT_REP_ADQ_BAJA = "ADQUISICIÓN \nBAJA";
   public static String TIT_REP_COSTO = "COSTO \nACTUALIZADO";
   public static String TIT_REP_DEPR = "DEP AÑO \nACTUALIZADA";
   public static String TIT_REP_CONCEPTO_REGION = "RESUMEN POR CONEPTO REGION";
   public static String TIT_REP_CONCEPTO_CLASE = "RESUMEN POR CONCEPTO CLASE";
   public static String TIT_REP_GLOB_REGION = "TOTAL GLOBAL POR REGION";
   public static String TIT_REP_GLOB_CLASE = "TOTAL GLOBAL POR CLASE";
   public static String TIT_INST_REGION = "INSTITUCIONAL POR REGION";
   public static String TIT_INST_CLASE = "INSTITUCIONAL POR CLASE";
   public static String TIT_INST_AJUSTE = "RESUMEN INSTITUCIONAL DE AJUSTES";
   public static String TIT_REP_DETALLADO = "REPORTE DETALLADO";
   public static String TIT_REP_DPR = "REPORTE 100% DEPRECIADOS";
   public static String TIT_REP_DETALLADO_AJUSTE = "REPORTE DETALLADO DE AJUSTES POR TIPO";
   public static String TIT_REP_CONCENTRADO = "REPORTE CONCENTRADO";
   public static String TIT_RES_AJUSTES = "RESUMEN DE AJUSTES";
   public static String TIT_MES="MES";
   public static String TIT_ANIO="AÑO";
   public static String TIT_CLASE="CLASE";
   public static String TIT_REGION="REGION";
   public static String TIT_TEXT_BAJA="TEXTO BAJA";
   public static String TIT_JUSTES="AJUSTES";
   public static String TIT_JUSTES_MOI="AJUSTES AL MOI";
   public static String TIT_JUSTES_TIPO="AJUSTES POR TIPO";
   public static String TIT_DEPRE_ANUAL="AJUSTES DEP. AÑO ACTUALIZADA";
   public static String TIT_JUSTES_COSTO="AJUSTES COSTO ACTUALIZADO";
   public static String TIT_GRAN_TOT_AJUST="GRAN TOTAL AJUSTADO";
   public static String TIT_RE_MES_ENE="ENE";
   public static String TIT_RE_MES_FEB="FEB";
   public static String TIT_RE_MES_MAR="MAR";
   public static String TIT_RE_MES_ABR="ABR";
   public static String TIT_RE_MES_MAY="MAY";
   public static String TIT_RE_MES_JUN="JUN";
   public static String TIT_RE_MES_JUL="JUL";
   public static String TIT_RE_MES_AGO="AGO";
   public static String TIT_RE_MES_SEP="SEP";
   public static String TIT_RE_MES_OCT="OCT";
   public static String TIT_RE_MES_NOV="NOV";
   public static String TIT_RE_MES_DIC="DIC";
   public static String TIT_RE_TOTAL="TOTAL";
   public static String TIT_RE_TOTAL_AJU_TPO="TOTAL AJUSTES POR TIPO";
   public static String TIT_RE_REGION="RESUMEN POR REGION";
   public static String REP_PDF_EXT=".pdf";
   
}
