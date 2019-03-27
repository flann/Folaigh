/* 
    Copyright 2005 Karmashave.org/Terry Lacy

    This file is part of Folaigh.

    Folaigh is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 2.1 of the License, or
    (at your option) any later version.

    Folaigh is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Folaigh; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Data;

using org.bouncycastle.crypto;
using org.bouncycastle.math;
using org.bouncycastle.x509;
using org.bouncycastle.asn1.x509;
using org.bouncycastle.crypto.generators;
using org.bouncycastle.crypto.parameters;
using org.bouncycastle.security;
using org.bouncycastle.pkcs;

using System.Threading;

namespace FolaighKeyManager
{
	/// <summary>
	/// Summary description for Form1.
	/// </summary>
	public class Form1 : System.Windows.Forms.Form
	{
		private System.Windows.Forms.GroupBox groupBox1;
		private System.Windows.Forms.TextBox txtClientAlias;
		private System.Windows.Forms.TextBox txtClientCommonName;
		private System.Windows.Forms.TextBox txtClientEmail;
		private System.Windows.Forms.TextBox txtClientLocality;
		private System.Windows.Forms.TextBox txtClientState;
		private System.Windows.Forms.TextBox txtClientOrganization;
		private System.Windows.Forms.TextBox txtClientCountry;
		private System.Windows.Forms.Label label7;
		private System.Windows.Forms.Label label6;
		private System.Windows.Forms.Label label5;
		private System.Windows.Forms.Label label4;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.GroupBox groupBox2;
		private System.Windows.Forms.TextBox txtServerAlias;
		private System.Windows.Forms.TextBox txtServerCommonName;
		private System.Windows.Forms.TextBox txtServerEmail;
		private System.Windows.Forms.TextBox txtServerLocality;
		private System.Windows.Forms.TextBox txtServerState;
		private System.Windows.Forms.TextBox txtServerOrganization;
		private System.Windows.Forms.TextBox txtServerCountry;
		private System.Windows.Forms.Label label8;
		private System.Windows.Forms.Label label9;
		private System.Windows.Forms.Label label10;
		private System.Windows.Forms.Label label11;
		private System.Windows.Forms.Label label12;
		private System.Windows.Forms.Label label13;
		private System.Windows.Forms.Label label14;
		private System.Windows.Forms.TextBox txtClientPassword;
		private System.Windows.Forms.Label label15;
		private System.Windows.Forms.Label label17;
		private System.Windows.Forms.Label label16;
		private System.Windows.Forms.Label label18;
		private System.Windows.Forms.TextBox txtClientPasswordConfirm;
		private System.Windows.Forms.TextBox txtServerPasswordConfirm;
		private System.Windows.Forms.TextBox txtServerPassword;
		private System.Windows.Forms.Button btnCreate;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;
		private System.Windows.Forms.FolderBrowserDialog folderBrowserDialog1;

		private EventHandler onKeyCreationComplete = null;

		public Form1()
		{
			//
			// Required for Windows Form Designer support
			//
			InitializeComponent();

			onKeyCreationComplete = new EventHandler(OnKeyCreationComplete);
		}

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if (components != null) 
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.groupBox1 = new System.Windows.Forms.GroupBox();
			this.txtClientPasswordConfirm = new System.Windows.Forms.TextBox();
			this.label17 = new System.Windows.Forms.Label();
			this.txtClientPassword = new System.Windows.Forms.TextBox();
			this.label15 = new System.Windows.Forms.Label();
			this.txtClientAlias = new System.Windows.Forms.TextBox();
			this.txtClientCommonName = new System.Windows.Forms.TextBox();
			this.txtClientEmail = new System.Windows.Forms.TextBox();
			this.txtClientLocality = new System.Windows.Forms.TextBox();
			this.txtClientState = new System.Windows.Forms.TextBox();
			this.txtClientOrganization = new System.Windows.Forms.TextBox();
			this.txtClientCountry = new System.Windows.Forms.TextBox();
			this.label7 = new System.Windows.Forms.Label();
			this.label6 = new System.Windows.Forms.Label();
			this.label5 = new System.Windows.Forms.Label();
			this.label4 = new System.Windows.Forms.Label();
			this.label3 = new System.Windows.Forms.Label();
			this.label2 = new System.Windows.Forms.Label();
			this.label1 = new System.Windows.Forms.Label();
			this.groupBox2 = new System.Windows.Forms.GroupBox();
			this.txtServerPasswordConfirm = new System.Windows.Forms.TextBox();
			this.label16 = new System.Windows.Forms.Label();
			this.txtServerPassword = new System.Windows.Forms.TextBox();
			this.label18 = new System.Windows.Forms.Label();
			this.txtServerAlias = new System.Windows.Forms.TextBox();
			this.txtServerCommonName = new System.Windows.Forms.TextBox();
			this.txtServerEmail = new System.Windows.Forms.TextBox();
			this.txtServerLocality = new System.Windows.Forms.TextBox();
			this.txtServerState = new System.Windows.Forms.TextBox();
			this.txtServerOrganization = new System.Windows.Forms.TextBox();
			this.txtServerCountry = new System.Windows.Forms.TextBox();
			this.label8 = new System.Windows.Forms.Label();
			this.label9 = new System.Windows.Forms.Label();
			this.label10 = new System.Windows.Forms.Label();
			this.label11 = new System.Windows.Forms.Label();
			this.label12 = new System.Windows.Forms.Label();
			this.label13 = new System.Windows.Forms.Label();
			this.label14 = new System.Windows.Forms.Label();
			this.btnCreate = new System.Windows.Forms.Button();
			this.folderBrowserDialog1 = new System.Windows.Forms.FolderBrowserDialog();
			this.groupBox1.SuspendLayout();
			this.groupBox2.SuspendLayout();
			this.SuspendLayout();
			// 
			// groupBox1
			// 
			this.groupBox1.Controls.Add(this.txtClientPasswordConfirm);
			this.groupBox1.Controls.Add(this.label17);
			this.groupBox1.Controls.Add(this.txtClientPassword);
			this.groupBox1.Controls.Add(this.label15);
			this.groupBox1.Controls.Add(this.txtClientAlias);
			this.groupBox1.Controls.Add(this.txtClientCommonName);
			this.groupBox1.Controls.Add(this.txtClientEmail);
			this.groupBox1.Controls.Add(this.txtClientLocality);
			this.groupBox1.Controls.Add(this.txtClientState);
			this.groupBox1.Controls.Add(this.txtClientOrganization);
			this.groupBox1.Controls.Add(this.txtClientCountry);
			this.groupBox1.Controls.Add(this.label7);
			this.groupBox1.Controls.Add(this.label6);
			this.groupBox1.Controls.Add(this.label5);
			this.groupBox1.Controls.Add(this.label4);
			this.groupBox1.Controls.Add(this.label3);
			this.groupBox1.Controls.Add(this.label2);
			this.groupBox1.Controls.Add(this.label1);
			this.groupBox1.Location = new System.Drawing.Point(20, 16);
			this.groupBox1.Name = "groupBox1";
			this.groupBox1.Size = new System.Drawing.Size(304, 320);
			this.groupBox1.TabIndex = 14;
			this.groupBox1.TabStop = false;
			this.groupBox1.Text = "Client";
			// 
			// txtClientPasswordConfirm
			// 
			this.txtClientPasswordConfirm.Location = new System.Drawing.Point(128, 281);
			this.txtClientPasswordConfirm.Name = "txtClientPasswordConfirm";
			this.txtClientPasswordConfirm.PasswordChar = '*';
			this.txtClientPasswordConfirm.Size = new System.Drawing.Size(152, 20);
			this.txtClientPasswordConfirm.TabIndex = 31;
			this.txtClientPasswordConfirm.Text = "";
			// 
			// label17
			// 
			this.label17.Location = new System.Drawing.Point(8, 280);
			this.label17.Name = "label17";
			this.label17.Size = new System.Drawing.Size(120, 23);
			this.label17.TabIndex = 30;
			this.label17.Text = "Password Confirm: ";
			this.label17.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// txtClientPassword
			// 
			this.txtClientPassword.Location = new System.Drawing.Point(128, 257);
			this.txtClientPassword.Name = "txtClientPassword";
			this.txtClientPassword.PasswordChar = '*';
			this.txtClientPassword.Size = new System.Drawing.Size(152, 20);
			this.txtClientPassword.TabIndex = 29;
			this.txtClientPassword.Text = "";
			// 
			// label15
			// 
			this.label15.Location = new System.Drawing.Point(8, 256);
			this.label15.Name = "label15";
			this.label15.Size = new System.Drawing.Size(120, 23);
			this.label15.TabIndex = 28;
			this.label15.Text = "Keystore Password: ";
			this.label15.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// txtClientAlias
			// 
			this.txtClientAlias.Location = new System.Drawing.Point(104, 213);
			this.txtClientAlias.Name = "txtClientAlias";
			this.txtClientAlias.Size = new System.Drawing.Size(176, 20);
			this.txtClientAlias.TabIndex = 27;
			this.txtClientAlias.Text = "";
			// 
			// txtClientCommonName
			// 
			this.txtClientCommonName.Location = new System.Drawing.Point(104, 183);
			this.txtClientCommonName.Name = "txtClientCommonName";
			this.txtClientCommonName.Size = new System.Drawing.Size(176, 20);
			this.txtClientCommonName.TabIndex = 26;
			this.txtClientCommonName.Text = "";
			// 
			// txtClientEmail
			// 
			this.txtClientEmail.Location = new System.Drawing.Point(104, 153);
			this.txtClientEmail.Name = "txtClientEmail";
			this.txtClientEmail.Size = new System.Drawing.Size(176, 20);
			this.txtClientEmail.TabIndex = 25;
			this.txtClientEmail.Text = "";
			// 
			// txtClientLocality
			// 
			this.txtClientLocality.Location = new System.Drawing.Point(104, 123);
			this.txtClientLocality.Name = "txtClientLocality";
			this.txtClientLocality.Size = new System.Drawing.Size(176, 20);
			this.txtClientLocality.TabIndex = 24;
			this.txtClientLocality.Text = "";
			// 
			// txtClientState
			// 
			this.txtClientState.Location = new System.Drawing.Point(104, 93);
			this.txtClientState.Name = "txtClientState";
			this.txtClientState.Size = new System.Drawing.Size(176, 20);
			this.txtClientState.TabIndex = 23;
			this.txtClientState.Text = "";
			// 
			// txtClientOrganization
			// 
			this.txtClientOrganization.Location = new System.Drawing.Point(104, 63);
			this.txtClientOrganization.Name = "txtClientOrganization";
			this.txtClientOrganization.Size = new System.Drawing.Size(176, 20);
			this.txtClientOrganization.TabIndex = 22;
			this.txtClientOrganization.Text = "";
			// 
			// txtClientCountry
			// 
			this.txtClientCountry.Location = new System.Drawing.Point(104, 33);
			this.txtClientCountry.Name = "txtClientCountry";
			this.txtClientCountry.Size = new System.Drawing.Size(176, 20);
			this.txtClientCountry.TabIndex = 21;
			this.txtClientCountry.Text = "";
			// 
			// label7
			// 
			this.label7.Location = new System.Drawing.Point(8, 212);
			this.label7.Name = "label7";
			this.label7.Size = new System.Drawing.Size(96, 23);
			this.label7.TabIndex = 20;
			this.label7.Text = "Alias: ";
			this.label7.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label6
			// 
			this.label6.Location = new System.Drawing.Point(8, 182);
			this.label6.Name = "label6";
			this.label6.Size = new System.Drawing.Size(96, 23);
			this.label6.TabIndex = 19;
			this.label6.Text = "Common Name: ";
			this.label6.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label5
			// 
			this.label5.Location = new System.Drawing.Point(8, 152);
			this.label5.Name = "label5";
			this.label5.Size = new System.Drawing.Size(96, 23);
			this.label5.TabIndex = 18;
			this.label5.Text = "Email Address: ";
			this.label5.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label4
			// 
			this.label4.Location = new System.Drawing.Point(8, 92);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(96, 23);
			this.label4.TabIndex = 17;
			this.label4.Text = "State/Province: ";
			this.label4.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label3
			// 
			this.label3.Location = new System.Drawing.Point(8, 122);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(96, 23);
			this.label3.TabIndex = 16;
			this.label3.Text = "Locality/City: ";
			this.label3.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label2
			// 
			this.label2.Location = new System.Drawing.Point(8, 62);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(96, 23);
			this.label2.TabIndex = 15;
			this.label2.Text = "Organization: ";
			this.label2.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label1
			// 
			this.label1.Location = new System.Drawing.Point(8, 32);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(96, 23);
			this.label1.TabIndex = 14;
			this.label1.Text = "Country: ";
			this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// groupBox2
			// 
			this.groupBox2.Controls.Add(this.txtServerPasswordConfirm);
			this.groupBox2.Controls.Add(this.label16);
			this.groupBox2.Controls.Add(this.txtServerPassword);
			this.groupBox2.Controls.Add(this.label18);
			this.groupBox2.Controls.Add(this.txtServerAlias);
			this.groupBox2.Controls.Add(this.txtServerCommonName);
			this.groupBox2.Controls.Add(this.txtServerEmail);
			this.groupBox2.Controls.Add(this.txtServerLocality);
			this.groupBox2.Controls.Add(this.txtServerState);
			this.groupBox2.Controls.Add(this.txtServerOrganization);
			this.groupBox2.Controls.Add(this.txtServerCountry);
			this.groupBox2.Controls.Add(this.label8);
			this.groupBox2.Controls.Add(this.label9);
			this.groupBox2.Controls.Add(this.label10);
			this.groupBox2.Controls.Add(this.label11);
			this.groupBox2.Controls.Add(this.label12);
			this.groupBox2.Controls.Add(this.label13);
			this.groupBox2.Controls.Add(this.label14);
			this.groupBox2.Location = new System.Drawing.Point(332, 16);
			this.groupBox2.Name = "groupBox2";
			this.groupBox2.Size = new System.Drawing.Size(304, 320);
			this.groupBox2.TabIndex = 15;
			this.groupBox2.TabStop = false;
			this.groupBox2.Text = "Server";
			// 
			// txtServerPasswordConfirm
			// 
			this.txtServerPasswordConfirm.Location = new System.Drawing.Point(128, 280);
			this.txtServerPasswordConfirm.Name = "txtServerPasswordConfirm";
			this.txtServerPasswordConfirm.PasswordChar = '*';
			this.txtServerPasswordConfirm.Size = new System.Drawing.Size(152, 20);
			this.txtServerPasswordConfirm.TabIndex = 35;
			this.txtServerPasswordConfirm.Text = "";
			// 
			// label16
			// 
			this.label16.Location = new System.Drawing.Point(8, 280);
			this.label16.Name = "label16";
			this.label16.Size = new System.Drawing.Size(120, 23);
			this.label16.TabIndex = 34;
			this.label16.Text = "Password Confirm: ";
			this.label16.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// txtServerPassword
			// 
			this.txtServerPassword.Location = new System.Drawing.Point(128, 256);
			this.txtServerPassword.Name = "txtServerPassword";
			this.txtServerPassword.PasswordChar = '*';
			this.txtServerPassword.Size = new System.Drawing.Size(152, 20);
			this.txtServerPassword.TabIndex = 33;
			this.txtServerPassword.Text = "";
			// 
			// label18
			// 
			this.label18.Location = new System.Drawing.Point(8, 256);
			this.label18.Name = "label18";
			this.label18.Size = new System.Drawing.Size(120, 23);
			this.label18.TabIndex = 32;
			this.label18.Text = "Keystore Password: ";
			this.label18.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// txtServerAlias
			// 
			this.txtServerAlias.Location = new System.Drawing.Point(104, 213);
			this.txtServerAlias.Name = "txtServerAlias";
			this.txtServerAlias.Size = new System.Drawing.Size(176, 20);
			this.txtServerAlias.TabIndex = 27;
			this.txtServerAlias.Text = "";
			// 
			// txtServerCommonName
			// 
			this.txtServerCommonName.Location = new System.Drawing.Point(104, 183);
			this.txtServerCommonName.Name = "txtServerCommonName";
			this.txtServerCommonName.Size = new System.Drawing.Size(176, 20);
			this.txtServerCommonName.TabIndex = 26;
			this.txtServerCommonName.Text = "";
			// 
			// txtServerEmail
			// 
			this.txtServerEmail.Location = new System.Drawing.Point(104, 153);
			this.txtServerEmail.Name = "txtServerEmail";
			this.txtServerEmail.Size = new System.Drawing.Size(176, 20);
			this.txtServerEmail.TabIndex = 25;
			this.txtServerEmail.Text = "";
			// 
			// txtServerLocality
			// 
			this.txtServerLocality.Location = new System.Drawing.Point(104, 123);
			this.txtServerLocality.Name = "txtServerLocality";
			this.txtServerLocality.Size = new System.Drawing.Size(176, 20);
			this.txtServerLocality.TabIndex = 24;
			this.txtServerLocality.Text = "";
			// 
			// txtServerState
			// 
			this.txtServerState.Location = new System.Drawing.Point(104, 93);
			this.txtServerState.Name = "txtServerState";
			this.txtServerState.Size = new System.Drawing.Size(176, 20);
			this.txtServerState.TabIndex = 23;
			this.txtServerState.Text = "";
			// 
			// txtServerOrganization
			// 
			this.txtServerOrganization.Location = new System.Drawing.Point(104, 63);
			this.txtServerOrganization.Name = "txtServerOrganization";
			this.txtServerOrganization.Size = new System.Drawing.Size(176, 20);
			this.txtServerOrganization.TabIndex = 22;
			this.txtServerOrganization.Text = "";
			// 
			// txtServerCountry
			// 
			this.txtServerCountry.Location = new System.Drawing.Point(104, 33);
			this.txtServerCountry.Name = "txtServerCountry";
			this.txtServerCountry.Size = new System.Drawing.Size(176, 20);
			this.txtServerCountry.TabIndex = 21;
			this.txtServerCountry.Text = "";
			// 
			// label8
			// 
			this.label8.Location = new System.Drawing.Point(8, 212);
			this.label8.Name = "label8";
			this.label8.Size = new System.Drawing.Size(96, 23);
			this.label8.TabIndex = 20;
			this.label8.Text = "Alias: ";
			this.label8.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label9
			// 
			this.label9.Location = new System.Drawing.Point(8, 182);
			this.label9.Name = "label9";
			this.label9.Size = new System.Drawing.Size(96, 23);
			this.label9.TabIndex = 19;
			this.label9.Text = "Common Name: ";
			this.label9.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label10
			// 
			this.label10.Location = new System.Drawing.Point(8, 152);
			this.label10.Name = "label10";
			this.label10.Size = new System.Drawing.Size(96, 23);
			this.label10.TabIndex = 18;
			this.label10.Text = "Email Address: ";
			this.label10.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label11
			// 
			this.label11.Location = new System.Drawing.Point(8, 92);
			this.label11.Name = "label11";
			this.label11.Size = new System.Drawing.Size(96, 23);
			this.label11.TabIndex = 17;
			this.label11.Text = "State/Province: ";
			this.label11.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label12
			// 
			this.label12.Location = new System.Drawing.Point(8, 122);
			this.label12.Name = "label12";
			this.label12.Size = new System.Drawing.Size(96, 23);
			this.label12.TabIndex = 16;
			this.label12.Text = "Locality/City: ";
			this.label12.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label13
			// 
			this.label13.Location = new System.Drawing.Point(8, 62);
			this.label13.Name = "label13";
			this.label13.Size = new System.Drawing.Size(96, 23);
			this.label13.TabIndex = 15;
			this.label13.Text = "Organization: ";
			this.label13.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// label14
			// 
			this.label14.Location = new System.Drawing.Point(8, 32);
			this.label14.Name = "label14";
			this.label14.Size = new System.Drawing.Size(96, 23);
			this.label14.TabIndex = 14;
			this.label14.Text = "Country: ";
			this.label14.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// btnCreate
			// 
			this.btnCreate.Location = new System.Drawing.Point(268, 352);
			this.btnCreate.Name = "btnCreate";
			this.btnCreate.Size = new System.Drawing.Size(120, 23);
			this.btnCreate.TabIndex = 16;
			this.btnCreate.Text = "Create Keystores";
			this.btnCreate.Click += new System.EventHandler(this.btnCreate_Click);
			// 
			// Form1
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(656, 390);
			this.Controls.Add(this.btnCreate);
			this.Controls.Add(this.groupBox2);
			this.Controls.Add(this.groupBox1);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Name = "Form1";
			this.Text = "Folaigh Key Manager";
			this.groupBox1.ResumeLayout(false);
			this.groupBox2.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main() 
		{
			Application.Run(new Form1());
		}

		PleaseWaitForm pleaseWaitForm = null;
		String keystoreFolder = null;
		private void btnCreate_Click(object sender, System.EventArgs e)
		{
			if ( !txtServerPassword.Text.Equals(txtServerPasswordConfirm.Text) )
			{
				MessageBox.Show(this,"The server passwords don't match.");
				return;
			}
			if ( !txtClientPassword.Text.Equals(txtClientPasswordConfirm.Text) )
			{
				MessageBox.Show(this,"The client passwords don't match.");
				return;
			}
			if ( folderBrowserDialog1.ShowDialog(this) == DialogResult.OK )
			{
				keystoreFolder = folderBrowserDialog1.SelectedPath;
			}
			else
			{
				return;
			}

			btnCreate.Enabled = false;
			pleaseWaitForm = new PleaseWaitForm();
			pleaseWaitForm.Show(); 
			Thread thread = new Thread(new ThreadStart(createRSAKeys));
			thread.Start();
		}

		private void createRSAKeys()
		{
			AsymmetricCipherKeyPair serverKey = generateRSAKey();
			AsymmetricCipherKeyPair clientKey = generateRSAKey();

			X509Certificate serverCert = createCertificate(
				serverKey,
				txtServerCountry.Text,
				txtServerOrganization.Text,
				txtServerLocality.Text,
				txtServerState.Text,
				txtServerEmail.Text,
				txtServerCommonName.Text,
				BigInteger.valueOf(DateTime.Now.Ticks),
				serverKey);

			X509Certificate clientCert = createCertificate(
				clientKey,
				txtClientCountry.Text,
				txtClientOrganization.Text,
				txtClientLocality.Text,
				txtClientState.Text,
				txtClientEmail.Text,
				txtClientCommonName.Text,
				BigInteger.valueOf(DateTime.Now.Ticks),
				clientKey);

			CreateKeystore(
				txtServerAlias.Text,
				txtClientAlias.Text,
				serverKey,
				serverCert,
				clientCert,
				txtServerAlias.Text,
				txtServerPassword.Text);

			CreateKeystore(
				txtClientAlias.Text,
				txtServerAlias.Text,
				clientKey,
				clientCert,
				serverCert,
				txtClientAlias.Text,
				txtClientPassword.Text);

			BeginInvoke(onKeyCreationComplete, new object[] {this, EventArgs.Empty});
		}

		private void CreateKeystore(
			String myAlias,
			String peerAlias,
			AsymmetricCipherKeyPair myKey,
			X509Certificate myCert,
			X509Certificate peerCert,
			String filename,
			String password)
		{
			PKCS12Store pkcs12 = new PKCS12Store();

			pkcs12.setKeyEntry(
				myAlias,
				new AsymmetricKeyEntry(myKey.getPrivate()),
				new X509CertificateEntry[] {new X509CertificateEntry(myCert)});
			pkcs12.setCertificateEntry(peerAlias,new X509CertificateEntry(peerCert));

			System.IO.FileStream stream = 
				new System.IO.FileStream(
				keystoreFolder + 
				"\\" + filename + ".p12",
				System.IO.FileMode.Create);
			pkcs12.save(stream,password.ToCharArray(),new SecureRandom());
			stream.Close();
		}

		private void OnKeyCreationComplete(object sender, EventArgs e)
		{
			if ( pleaseWaitForm != null )
			{
				pleaseWaitForm.Hide();
				pleaseWaitForm.Close();
				pleaseWaitForm = null;
			}
			btnCreate.Enabled = true;
		}

		private static X509Certificate createCertificate(
			AsymmetricCipherKeyPair pair,
			string country,
			string organization,
			string locality,
			string state,
			string emailAddress,
			string commonName,
			BigInteger serialNumber,
			AsymmetricCipherKeyPair signer)
		{
			Hashtable                   attrs = new Hashtable();

			attrs.Add(X509Name.C, country);				// Country
			attrs.Add(X509Name.O, organization);	// Organization
			attrs.Add(X509Name.L, locality);	// Locality
			attrs.Add(X509Name.ST, state);				// State/Province
			attrs.Add(X509Name.EmailAddress, emailAddress);
			attrs.Add(X509Name.CN, commonName);		// Common Name

			// Create a certificate
			X509V3CertificateGenerator  certGen = new X509V3CertificateGenerator();
			certGen.setSerialNumber(serialNumber);
			certGen.setIssuerDN(new X509Name(attrs));
			certGen.setNotBefore(DateTime.Today.Subtract(new TimeSpan(1, 0, 0, 0)));
			certGen.setNotAfter(DateTime.Today.AddDays(365));
			certGen.setSubjectDN(new X509Name(attrs));
			certGen.setPublicKey(pair.getPublic());
			certGen.setSignatureAlgorithm("SHA1WithRSAEncryption");

			return certGen.generateX509Certificate(signer.getPrivate());
		}
		private AsymmetricCipherKeyPair generateRSAKey()
		{
			RSAKeyPairGenerator  pGen = new RSAKeyPairGenerator();
			RSAKeyGenerationParameters  genParam = new RSAKeyGenerationParameters(
				BigInteger.valueOf(0x11), // public exponent
				new SecureRandom(),		  // Random number generator
				1024,					  // Strength (bit size)
				1024);					  // Certainty (of prime numbers) (1 - 1/(2^certainty))
			//				10);					  // Certainty (of prime numbers) (1 - 1/(2^certainty))

			pGen.init(genParam);

			return pGen.generateKeyPair();
		}
	}
}
