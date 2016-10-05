package com.lyq.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.lyq.dao.MedicineDao;
import com.lyq.util.StringUtil;
/**
 * ҩƷ����ɾ��Action��
 * LookupDispatchAction������
 * @author Li Yong Qiang
 */
public class DeleteMedicineAction extends DeleteAction {
	MedicineDao dao = new MedicineDao();
	//ɾ��ȫ��
	public ActionForward all(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		// ��ȡҩƷid
		String[] ids = arg2.getParameterValues("allId");
		// ��Ч����֤
		if(ids != null && ids.length > 0){
			// ɾ��ȫ��
			dao.deleteByHQL("delete Medicine d where d.id in("+StringUtil.arr2Str(ids)+")");
		}
		return arg0.findForward("findAllSuccess");
	}
	//ɾ����ѡ
	public ActionForward selected(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		// ��ȡҩƷid
		String[] ids = arg2.getParameterValues("selectedId");
		// ��Ч����֤
		if(ids != null && ids.length > 0){
			// ɾ����ѡ
			dao.deleteByHQL("delete Medicine d where d.id in("+StringUtil.arr2Str(ids)+")");
		}
		return arg0.findForward("findAllSuccess");
	}
}
