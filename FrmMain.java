package com.gui.in;

import java.util.regex.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Font;

public class FrmMain extends JFrame {
	String COMMAND = "ip route";
	public String gateway = null;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmMain frame = new FrmMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public FrmMain() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);

		JButton btnPing = new JButton("Ping");
		panel.add(btnPing);

		JButton btnTrace = new JButton("Trace");
		panel.add(btnTrace);

		JButton btnDetails = new JButton("Details");
		panel.add(btnDetails);

		JPanel pnlInfos = new JPanel();
		contentPane.add(pnlInfos, BorderLayout.CENTER);
		pnlInfos.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("74px"),
						FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("14px"), FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblIPv4 = new JLabel("Address IPv4 : ");
		lblIPv4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pnlInfos.add(lblIPv4, "4, 2, default, top");

		lblIPv4.setText(getLocalAddressIPv4());

		JLabel lblGateway = new JLabel("Gateway : ");
		lblGateway.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pnlInfos.add(lblGateway, "4, 4");

		JLabel lblNetmask = new JLabel("Netmask : ");
		lblNetmask.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pnlInfos.add(lblNetmask, "4, 6");

		List<String> output = GetGatewayAddress();
		lblGateway.setText("Gateway : " + output.get(0));
		lblIPv4.setText("IPv4 : " + output.get(2));
		lblNetmask.setText("Submask network : " + output.get(3));
	}

	public String getLocalAddressIPv4() throws UnknownHostException {
		InetAddress IP = InetAddress.getLocalHost();
		return ("IPv4 address : " + IP.getHostAddress());
	}

	public List<String> GetGatewayAddress() throws IOException {
		String matchSubnetMask[] = { "0.0.0.0", "128.0.0.0", "192.0.0.0", "224.0.0.0", "240.0.0.0", "248.0.0.0",
				"252.0.0.0", "254.0.0.0", "255.0.0.0", "255.128.0.0", "255.192.0.0", "255.224.0.0", "255.240.0.0",
				"255.248.0.0", "255.252.0.0", "255.254.0.0", "255.255.0.0", "255.255.128.0", "255.255.192.0",
				"255.255.224.0", "255.255.240.0", "255.255.248.0", "255.255.252.0", "255.255.254.0", "255.255.255.0",
				"255.255.255.128", "255.255.255.192", "255.255.255.224", "255.255.255.240", "255.255.255.248",
				"255.255.255.252", "255.255.255.254" };
		String submask = "";
		Pattern pat;
		Matcher match;
		List<String> result = new ArrayList<String>();

		Process p = Runtime.getRuntime().exec(COMMAND);
		InputStream i = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(i);
		BufferedReader br = new BufferedReader(isr);

		pat = Pattern.compile(
				"\\b(?:(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9]))\\b");
		String line = br.readLine();

		while (line != null) {
			match = pat.matcher(line);
			while (match.find()) {
				result.add(match.group().toString());
					if (line.contains("/")) {
						submask = matchSubnetMask[Integer.parseInt(line.split("/")[1].split(" ")[0])];
						//result.add(submask);
						submask=null;

					}
			}
			line = br.readLine();
		}

		// index : 0 => Gateway
		// 1 => Network
		// 2 => Host 
		// 3 => Submask network
		for (String results : result) {
			System.out.println(results);

		}
		return result;
	}

}
