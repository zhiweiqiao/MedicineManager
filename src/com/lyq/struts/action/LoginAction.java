package com.lyq.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.lyq.dao.UserDao;
import com.lyq.persistence.User;
import com.lyq.struts.form.UserForm;

/**
 * �û���¼Action��
 * 
 * @author Li Yong Qiang
 */
public class LoginAction extends Action {
	// �����û���¼����
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm uf = (UserForm) form;		// ��ȡActionForm
		String userName = uf.getUsername();	// �û���
		String password = uf.getPassword();	// ����
		User user = null;
		// ��ѯ�û�
		if (userName != null && password != null) {
			UserDao userDao = new UserDao();
			user = userDao.login(userName, password);
		}
		// ��ѯ���û����¼�ɹ��������¼ʧ�ܷ��ص���¼ҳ��
		if (user != null) {
			request.getSession().setAttribute("user", user);
			return mapping.findForward("manage");
		} else {
			request.setAttribute("error", "error");
			return mapping.findForward("loginFail");
		}
	}
}
