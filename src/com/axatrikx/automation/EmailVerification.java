package com.axatrikx.automation;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;

public class EmailVerification {

	public Message[] getIMAPEmail(String host, String userName, String password) {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		try {
			Store store = session.getStore();
			store.connect(host, userName, password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			return inbox.getMessages();
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Message[] getPOP3Email(String host, String userName, String password) {
		Properties properties = new Properties();
		properties.put("mail.pop3.host", host);
		// properties.put("mail.pop3.port", "995"); PORT IF REQUIRED
		Session session = Session.getDefaultInstance(properties);
		Store store;
		try {
			store = session.getStore("pop3");
			store.connect(host, userName, password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			return inbox.getMessages();
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Message getMessage(Message[] messages, String expectedSubject) {
		for (Message msg : messages) {
			try {
				if (msg.getSubject().contains(expectedSubject)) {
					return msg;
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String imapHost = "imap.googlemail.com";
		String pop3Host = "pop.gmail.com";
		String userName = "<username>";
		String password = "<password>";
		EmailVerification ev = new EmailVerification();
		Message imapMsg = ev.getMessage(ev.getIMAPEmail(imapHost, userName, password), "<Your Expected Subject>");
		Message pop3Msg = ev.getMessage(ev.getPOP3Email(pop3Host, userName, password), "<Your Expected Subject>");
		try {
			System.out.println("Subject: " + imapMsg.getSubject());
			System.out.println("From: " + imapMsg.getFrom()[0]);
			// For IMAP emails, getContent() marks the email as Read.
			// imapMsg.setFlag(Flag.SEEN, true); // or use this.
			System.out.println("Text: " + imapMsg.getContent().toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
