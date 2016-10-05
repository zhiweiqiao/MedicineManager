package com.lyq.struts.action;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.lyq.dao.CategoryDao;
import com.lyq.dao.MedicineDao;
import com.lyq.persistence.Category;
import com.lyq.persistence.Medicine;
import com.lyq.struts.form.MedicineForm;
import com.lyq.util.QueryUtil;
import com.lyq.util.StringUtil;

/**
 * ҩƷAction��
 * @author Li Yong Qiang
 */
public class MedicineAction extends BaseAction {

	// ���ҩƷ
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Medicine med = null;
		// ��ȡMedicineForm
		MedicineForm df = (MedicineForm) form;
		MedicineDao dao = new MedicineDao();
		if (df.getMedCount() == 0) {
			df.setMedCount(1);
		}
		med = new Medicine();
		BeanUtils.copyProperties(med, df);
		CategoryDao cd = new CategoryDao();
		Category category = cd.loadCategory(df.getCategoryId());
		med.setCategory(category);
		// �ϴ�ͼƬ
		FormFile photo = df.getPhoto();
		if (photo != null && photo.getFileSize() > 0) {
			String realPath = this.getServlet().getServletContext()
					.getRealPath("/upload");
			// ���ļ��ϴ�����������ָ�����ļ���
			try {
				String fname = photo.getFileName(); // ��ȡ�ϴ��ļ�����
				if (fname.indexOf(".") != -1) {
					// ��ȡ�ļ�����չ��
					String endWith = fname.substring(fname.lastIndexOf("."),fname.length());
					// �õ�ǰ��ʱ�������ļ�����
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					fname = sdf.format(new Date()) + endWith;
				}
				// ����д���������ַ�����������
				OutputStream out = new FileOutputStream(realPath + "/" + fname); 
				out.write(photo.getFileData());	// ���ļ�д�뵽������
				out.flush();
				out.close();
				// ���ļ����Ʊ��浽���ݿ�
				med.setPhotoPath(fname);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dao.saveOrUpdate(med);
		return mapping.findForward("addSuccess");
	}
	// ���ҩƷ
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��ȡMedicineForm
		MedicineForm df = (MedicineForm) form;
		MedicineDao dao = new MedicineDao();
		// ҩƷ�Ѵ��ڣ�����ҩƷ����
		Medicine med = dao.loadMedicine(df.getId());
		int medCount = med.getMedCount();
		med.setMedCount(medCount + df.getMedCount());
		return mapping.findForward("addSuccess");
	}
	// ����medNo��ѯҩƷ
	public ActionForward findMedicineByMedNo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��ȡMedicineForm
		MedicineForm df = (MedicineForm) form;
		Medicine med = null;
		// ��ѯҩƷ
		if (df != null && df.getMedNo() != null) {
			MedicineDao dao = new MedicineDao();
			med = dao.findMedicineByMedNo(df.getMedNo());
		}
		// ���ҩƷ���ڣ��������������������ҩ
		if (med != null) {
			BeanUtils.copyProperties(df, med);
			request.setAttribute("med", "med");
			return mapping.findForward("medUpdate");
		} else {
			CategoryDao cd = new CategoryDao();
			List list = cd.findByHQL("from Category");
			request.setAttribute("cs", list);
			return mapping.findForward("medSave");
		}
	}

	// ��ҳ��ѯҩƷ��Ϣ
	public ActionForward paging(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��ȡҳ��
		String currPage = request.getParameter("currPage");
		// ����action��ַ
		String action = request.getContextPath() + "/baseData/med.do?command=paging";
		// HQL��ѯ���
		String hql = "from Medicine";
		// ��ҳ��ѯ������Map����
		Map map = this.getPage(hql, recPerPage, currPage, action, null);
		//��������ŵ�request��
		request.setAttribute("list", map.get("list"));
		//��������ŵ���ҳ����
		request.setAttribute("pagingBar", map.get("bar"));
		return mapping.findForward("findAllSuccess");
	}

	// �鿴ҩƷ��ϸ��Ϣ
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		Medicine med = null;
		// ����id��ѯҩƷ��Ϣ
		if (id != null && !"".equals(id)) {
			MedicineDao dao = new MedicineDao();
			med = dao.loadMedicineAndCategory(Integer.parseInt(id));
		}
		request.setAttribute("med", med);
		return mapping.findForward("view");
	}

	// �༭ҩƷ��Ϣ
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��ȡMedicineForm
		MedicineForm df = (MedicineForm) form;
		Medicine med = null;
		// ����id��ѯҩƷ
		if (df.getId() > 0) {
			MedicineDao dao = new MedicineDao();
			med = dao.loadMedicineAndCategory(df.getId());
			BeanUtils.copyProperties(df, med);
			df.setCategoryId(med.getCategory().getId());
		}
		// ��ȡ�����Ϣ
		CategoryDao categoryDao = new CategoryDao();
		List cs = categoryDao.findByHQL("from Category");
		request.setAttribute("cs", cs);
		return mapping.findForward("medSave");
	}

	// ��������ѯ����ҳ��ʾ���
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��ȡMedicineForm
		MedicineForm df = (MedicineForm) form;
		String currPage = request.getParameter("currPage");
		// �����ѯAction
		String action = request.getContextPath() + "/baseData/med.do?command=query";
		//����HQL��估��ҳ����
		Map mapQuery = QueryUtil.queryMedicine(df, currPage, action);
		String hql = (String) mapQuery.get("hql");
		action = (String) mapQuery.get("action");
		Object[] where = (Object[]) mapQuery.get("where");
		// ��ҳ��ѯ
		Map map = this.getPage(hql, recPerPage, currPage, action, where);
		//��������ŵ�request��
		request.setAttribute("list", map.get("list"));
		//��������ŵ���ҳ����
		request.setAttribute("pagingBar", map.get("bar"));
		return mapping.findForward("findAllSuccess");
	}

	// ģ����ѯ
	public ActionForward blurQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��ȡ�ؼ���
		String keyWord = request.getParameter("keyWord");
		// ��ȡ��ǰҳ
		String currPage = request.getParameter("currPage");
		String hql = "from Medicine d ";
		Object[] where = null;
		String action = request.getContextPath() + "/baseData/med.do?command=blurQuery";
		// ת��
		if (currPage != null && !currPage.isEmpty()) {
			keyWord = StringUtil.encodeZh(keyWord);
		}
		if (keyWord != null && !keyWord.isEmpty()) {
			action += "&keyWord=" + StringUtil.encodeURL(keyWord);
			keyWord = "%" + keyWord + "%";
			hql += " where d.name like ? or d.medNo like ? or d.factoryAdd like ? or d.description like ?";
			where = new Object[] { keyWord, keyWord, keyWord, keyWord };
		}
		// ��ҳ��ѯ
		Map map = this.getPage(hql, recPerPage, currPage, action, where);
		//��������ŵ�request��
		request.setAttribute("list", map.get("list"));
		//��������ŵ���ҳ����
		request.setAttribute("pagingBar", map.get("bar"));
		return mapping.findForward("findAllSuccess");
	}

	// ��������ҩƷ
	public ActionForward canSellMeds(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// HQL��ѯ����
		String hql = "from Medicine d where d.medCount > 0";
		// ��ȡ��ǰҳ
		String currPage = request.getParameter("currPage");
		String action = request.getContextPath() + "/baseData/med.do?command=canSellMeds";
		// ��ҳ��ѯ
		Map map = this.getPage(hql, recPerPage, currPage, action, null);
		//��������ŵ�request��
		request.setAttribute("list", map.get("list"));
		//��������ŵ���ҳ����
		request.setAttribute("pagingBar", map.get("bar"));
		return mapping.findForward("canSellMeds");
	}

	// ��ѯ���
	public ActionForward QueryMedCount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��ȡ��ѯ����
		String count = request.getParameter("count");
		// ��ȡ��ѯ���ͣ�>��<��=��
		String type = request.getParameter("type");
		// ��ȡ��ǰҳ
		String currPage = request.getParameter("currPage");
		Object[] where = null;
		String hql = "from Medicine d";
		String action = request.getContextPath() + "/require/require.do?command=blurQuery";
		if (count != null && type != null) {
			// ��������
			if (type.equals("0")) {
				hql = "from Medicine d where d.medCount = ?";
			} else if (type.equals("1")) {
				hql = "from Medicine d where d.medCount > ?";
			} else if (type.equals("-1")) {
				hql = "from Medicine d where d.medCount < ?";
			}
			action = action + "&type=" + type + "&count=" + count;
			where = new Object[] { new Integer(count) };
		}
		// ��ҳ��ѯ
		Map map = this.getPage(hql, recPerPage, currPage, action, where);
		//��������ŵ�request��
		request.setAttribute("list", map.get("list"));
		//��������ŵ���ҳ����
		request.setAttribute("pagingBar", map.get("bar"));
		return mapping.findForward("findAllSuccess");
	}
}
