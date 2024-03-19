package com.telcel.gsa.dsaf.converter;

import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;
import com.telcel.gsa.dsaf.bean.RegionBean;
import com.telcel.gsa.dsaf.bean.SessionBean;
/**
 * 
 * @author VI5XXAA
 *
 */
@FacesConverter(value="regionConverter")
public class RegionConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		SessionBean sessionBean = (SessionBean) session.getAttribute("session");
		List<RegionBean> regiones = sessionBean.getRegiones();
		Iterator<RegionBean> iterator = regiones.iterator();
		
        while(iterator.hasNext()) {
            RegionBean region = iterator.next();
            if(region.getClaveRegion().equalsIgnoreCase(value)) {
                return region;
            }
        }
		return null;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value != null) ? value.toString().toUpperCase() : "";
	}
	

}
