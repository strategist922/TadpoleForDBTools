/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.DBOperationType;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.hangum.tadpole.mongodb.core.connection.MongoDBNotFoundException;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.util.DBLocaleUtils;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.ApplicationArgumentUtils;

/**
 * oracle login composite
 * 
 * @author hangum
 *
 */
public class MongoDBLoginComposite extends AbstractLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8245123047846049939L;
	private static final Logger logger = Logger.getLogger(MongoDBLoginComposite.class);
	
	protected Combo comboGroup;
	protected Text textDisplayName;
	protected Combo comboOperationType;
	
	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	protected Combo comboLocale;
	
	protected Button btnSavePreference;
	private Text textReplicaSet;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MongoDBLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super(DBDefine.MONGODB_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
		setText(DBDefine.MONGODB_DEFAULT.getDBToString());
	}
	
	@Override
	public void crateComposite() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblOperationType = new Label(compositeBody, SWT.NONE);
		lblOperationType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOperationType.setText(Messages.MySQLLoginComposite_lblOperationType_text);
		
		comboOperationType = new Combo(compositeBody, SWT.READ_ONLY);
		comboOperationType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (DBOperationType opType : DBOperationType.values()) {
			comboOperationType.add(opType.getTypeName());
		}
		comboOperationType.select(1);
		
		Label lblGroupName = new Label(compositeBody, SWT.NONE);
		lblGroupName.setText(Messages.MySQLLoginComposite_lblGroupName_text);
		comboGroup = new Combo(compositeBody, SWT.NONE);
		comboGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (String strGroup : listGroupName) comboGroup.add(strGroup);
		
		Label lblNewLabel_1 = new Label(compositeBody, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText(Messages.DBLoginDialog_lblNewLabel_1_text);
		
		textDisplayName = new Text(compositeBody, SWT.BORDER);
		textDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(compositeBody, SWT.NONE);		
		new Label(compositeBody, SWT.NONE);
		
		Label lblHost = new Label(compositeBody, SWT.NONE);
		lblHost.setText(Messages.DBLoginDialog_1);
		
		textHost = new Text(compositeBody, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabelPort = new Label(compositeBody, SWT.NONE);
		lblNewLabelPort.setText(Messages.DBLoginDialog_5);
		
		textPort = new Text(compositeBody, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblReplicaSet = new Label(compositeBody, SWT.NONE);
		lblReplicaSet.setText(Messages.MongoDBLoginComposite_lblReplicaSet_text);
		
		textReplicaSet = new Text(compositeBody, SWT.BORDER);
		textReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		
		Label lblExLocalhostlocalhost = new Label(compositeBody, SWT.NONE);
		lblExLocalhostlocalhost.setText(Messages.MongoDBLoginComposite_lblExLocalhostlocalhost_text);
		
		Label lblNewLabelDatabase = new Label(compositeBody, SWT.NONE);
		lblNewLabelDatabase.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		lblNewLabelDatabase.setText(Messages.DBLoginDialog_4);
		
		textDatabase = new Text(compositeBody, SWT.BORDER);
		textDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		
		
		Label lblUser = new Label(compositeBody, SWT.NONE);
		lblUser.setText(Messages.DBLoginDialog_2);
		
		textUser = new Text(compositeBody, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(compositeBody, SWT.NONE);
		lblPassword.setText(Messages.DBLoginDialog_3);
		
		textPassword = new Text(compositeBody, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLocale = new Label(compositeBody, SWT.NONE);
		lblLocale.setText(Messages.MySQLLoginComposite_lblLocale_text);
		
		comboLocale = new Combo(compositeBody, SWT.READ_ONLY);
		comboLocale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
		for(String val : DBLocaleUtils.getMySQLList()) comboLocale.add(val);
		comboLocale.setVisibleItemCount(12);
		comboLocale.select(0);
		
		Button btnPing = new Button(compositeBody, SWT.NONE);
		btnPing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host 	= StringUtils.trimToEmpty(textHost.getText());
				String port 	= StringUtils.trimToEmpty(textPort.getText());
				
				if("".equals(host) || "".equals(port)) { //$NON-NLS-1$ //$NON-NLS-2$
					MessageDialog.openError(null, Messages.DBLoginDialog_10, Messages.DBLoginDialog_11);
					return;
				}
				
				try {
					if(isPing(host, port)) {
						MessageDialog.openInformation(null, Messages.DBLoginDialog_12, Messages.DBLoginDialog_13);
					} else {
						MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.DBLoginDialog_15);
					}
				} catch(NumberFormatException nfe) {
					MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
				}
			}
		});
		btnPing.setText(Messages.DBLoginDialog_btnPing_text);
		
		btnSavePreference = new Button(compositeBody, SWT.CHECK);
		btnSavePreference.setText(Messages.MySQLLoginComposite_btnSavePreference_text);
		btnSavePreference.setSelection(true);

		init();
	}
	
	@Override
	public void init() {

		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			textDisplayName.setText(oldUserDB.getDisplay_name());
			comboOperationType.setText( DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
			
			textHost.setText(oldUserDB.getHost());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			textDatabase.setText(oldUserDB.getDb());
			textPort.setText(oldUserDB.getPort());
			
			textReplicaSet.setText(oldUserDB.getExt1()==null?"":oldUserDB.getExt1());
		} else if(ApplicationArgumentUtils.isTestMode()) {

			textDisplayName.setText("Sample MongoDB 2.0.2"); //$NON-NLS-1$
			
			textHost.setText("127.0.0.1"); //$NON-NLS-1$
			textUser.setText(""); //$NON-NLS-1$
			textPassword.setText(""); //$NON-NLS-1$
			textDatabase.setText("test"); //$NON-NLS-1$
			textPort.setText("27017");			 //$NON-NLS-1$
		}
		
		if(comboGroup.getItems().length == 0) {
			comboGroup.add(strOtherGroupName);
			comboGroup.select(0);
		} else {
			if("".equals(selGroupName)) { //$NON-NLS-1$
				comboGroup.setText(strOtherGroupName);
			} else {
				for(int i=0; i<comboGroup.getItemCount(); i++) {
					if(comboGroup.getItem(i).equals(selGroupName)) comboGroup.select(i);
				}
			}
		}
	}
	
	public boolean isValidate() {
		
		if(!message(comboGroup, "Group")) return false; //$NON-NLS-1$
		if(!message(textHost, "Host")) return false; //$NON-NLS-1$
		if(!message(textPort, "Port")) return false; //$NON-NLS-1$
		if(!message(textDatabase, "Database")) return false; //$NON-NLS-1$		
		if(!message(textDisplayName, "Display Name")) return false; //$NON-NLS-1$
		
		String host 	= StringUtils.trimToEmpty(textHost.getText());
		String port 	= StringUtils.trimToEmpty(textPort.getText());
		
		// replica set
		String strReplcaSet = textReplicaSet.getText();
		String[] urls = StringUtils.split(strReplcaSet, ","); //$NON-NLS-1$
		for (String ipPort : urls) {
			String[] strIpPort = StringUtils.split(ipPort, ":"); //$NON-NLS-1$
			
			try {
				if(!isPing(strIpPort[0], strIpPort[1])) {
					MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.MongoDBLoginComposite_2 + strIpPort[0] + ":" + strIpPort[1]); //$NON-NLS-2$
					return false;
				}
			} catch(NumberFormatException nfe) {
				MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
				return false;
			} catch(ArrayIndexOutOfBoundsException aobe) {
				MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MongoDBLoginComposite_4);
				return false;
			}
		}
		
		try {
			if(!isPing(host, port)) {
				MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.MySQLLoginComposite_8);
				return false;
			}
		} catch(NumberFormatException nfe) {
			MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		final String dbUrl = String.format(
				DBDefine.MONGODB_DEFAULT.getDB_URL_INFO(), 
				textHost.getText(), textPort.getText(), textDatabase.getText());

		userDB = new UserDBDAO();
		userDB.setTypes(DBDefine.MONGODB_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText());
		userDB.setGroup_name(comboGroup.getText().trim());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setOperation_type( DBOperationType.getNameToType(comboOperationType.getText()).toString() );
		userDB.setHost(textHost.getText());
		userDB.setPasswd(textPassword.getText());
		userDB.setPort(textPort.getText());
		userDB.setLocale(comboLocale.getText().trim());
		userDB.setUsers(textUser.getText());
		userDB.setExt1(textReplicaSet.getText().trim());
		
		// 기존 데이터 업데이트
		if(oldUserDB != null) {
			if(!MessageDialog.openConfirm(null, "Confirm", Messages.SQLiteLoginComposite_13)) return false; //$NON-NLS-1$
			
			if(!checkDatabase(userDB)) return false;
			
			try {
				TadpoleSystem_UserDBQuery.updateUserDB(userDB, oldUserDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error(Messages.SQLiteLoginComposite_8, e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.SQLiteLoginComposite_5, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
		// 신규 데이터 저장.
		} else {

			// db가 정상적인지 채크해본다 
			try {
				MongoConnectionManager.getInstance(userDB);
			} catch(MongoDBNotFoundException mdbfe) {
				
				if(MessageDialog.openConfirm(null, "Confirm", userDB.getDb() + Messages.MongoDBLoginComposite_9)) { //$NON-NLS-1$
					try {
						MongoDBQuery.createDB(userDB);
					} catch (Exception e) {
						logger.error("MongoDB Connection error", e); //$NON-NLS-1$
						Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
						ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_10, errStatus); //$NON-NLS-1$
						
						return false;
					}
				} else {
					return false;
				}
				
	
			} catch (Exception e) {
				logger.error("MongoDB Connection error", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_10, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
			// preference에 save합니다.
			if(btnSavePreference.getSelection()) {
				try {
					TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
				} catch (Exception e) {
					logger.error("MongoDB info save", e); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getShell(), "Error", "MongoDB", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return true;
	}

}
