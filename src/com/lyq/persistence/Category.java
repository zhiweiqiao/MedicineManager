package com.lyq.persistence;

import java.util.Date;
import java.util.Set;
/**
 * ҩƷ���־û���
 * @author Li Yong Qiang
 */
public class Category {
	private int id;			//�����
	private String name;	//�������
	private String description;	//�������
	private String subDesc;		//���������ǰ10���ַ�
	private Date createTime;	//��𴴽�ʱ��
	private Set meds;			//����е�ҩƷ
	
	
	public String getSubDesc() {
		return subDesc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
		//��ȡ������ǰ10���ַ�
		if(description.length() > 10){
			this.subDesc = description.substring(0,10) + "...";
		}else{
			this.subDesc = description;
		}
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Set getMeds() {
		return meds;
	}
	public void setMeds(Set meds) {
		this.meds = meds;
	}
}
