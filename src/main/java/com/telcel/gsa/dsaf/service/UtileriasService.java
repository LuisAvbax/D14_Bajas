package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.BajasTresBean;
import com.telcel.gsa.dsaf.bean.BajasTresBeanResumen;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;




/**
 * 
 * @author VI5XXAA
 *
 */
public interface UtileriasService extends Serializable{
public Map<String, BajasCatParametrosBean> obtieneParametros(List<String> params);
public String obtieneRuta(String fileName);
public AdqBajasBean listCeroAdq(AdqBajasBean datos,String tipoLista);
public CostoBean listCeroCosto(CostoBean datos,String tipoLista);
public DepreActBean listCeroDepre(DepreActBean datos,String tipoLista);
public List<BajasDosBean> listBajasDos(List<BajasDosBean> listdatos);
public List<BajasTresBean> listBajasTres(List<BajasTresBean> listdatos);
public List<BajasTresBeanResumen> listBajasTresRes(List<BajasTresBeanResumen> listdatos);
public List<BajasDosBean> listBajasDosResumen(List<BajasDosBean> listdatos);


}
