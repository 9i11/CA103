package com.memsplike.controller;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.memsplike.model.*;

public class MemSpLikeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		super.doGet(req, resp);
	}
	
	/*
	 * ���ѥH�U�X�ӥ\��
	 * ��ӷs�W
	 * ��ӧ�s
	 * */
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		
		// �]�w�ШD�s�X
		req.setCharacterEncoding("UTF-8");
		// ���o�ШD�����Ѽ�"action"�H���D�O����I�sservlet��
		String action = req.getParameter("action");
		
		// �P�_�O����action,�ð���۹�{��
		// �̷�memid �C�X�Ҧ��ӷ|���P���쪺�B��
		// �bdao���j�M����ڦ��["�u�C�P����"���ԭz, �n��A��
		if ( "get_list_by_mem_id".equals(action) ) {
			// �Ψ��x�s���~�T��
			List<String> errorMsgs = new ArrayList<String>();
			// �brequset scope���[�J errorMsgs, ��ڭ̭n�^�ǿ��~�T�������X�n�D��������
			// �i�H�q������getAttribute errorMsgs�H�o����~��T
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.�����ШD�Ѽ� - ��J�榡�����~�B�z**********************/
				String str = (String)req.getAttribute("mem_id");
				if ( str == null || (str.trim()).length() == 0 ) {
					errorMsgs.add("�п�J�|��ID");
				}
				else {
					
				}
			}
			catch(Exception e) {
				
			}
		}
		
		// ��ӹB�ʳߦn�s�W
		if ("getOne_For_Update".equals(action)) {
			// �Ψ��x�s���~�T��
			List<String> errorMsgs = new ArrayList<String>();
			// �brequset scope���[�J errorMsgs, ��ڭ̭n�^�ǿ��~�T�������X�n�D��������
			// �i�H�q������getAttribute errorMsgs�H�o����~��T
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				MemSpLikeService memService = (MemSpLikeService)req.getAttribute("memSpLikeVO");
			}
			catch(Exception e) {
				
			}
		}

		super.doPost(req, res);
	}

}
