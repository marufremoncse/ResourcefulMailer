package email;

import db.DBHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.util.Date;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author marufur
 */

public class Email {
    public static void main(String[] args) throws FileNotFoundException, SQLException {

        DateFormat df = new SimpleDateFormat("dd_MM_yyyy");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj)); 
        
        DBHandler dbhandler = new DBHandler();
        
        String to = "marufremon.cse14@gmail.com";
        String from = "codingsense.2020@gmail.com";

        final String username = "codingsense.2020@gmail.com";
        final String password = "*******";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            PrintWriter pw = new PrintWriter(new File("E:\\MoMagic\\DEMO.csv"));
            StringBuilder sb = new StringBuilder();

            Connection con = dbhandler.getDb_con();

            String query = "select * from mydb.customers";
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery(query);

            while(rs.next()){
                sb.append(rs.getString("name"));
                sb.append(","); 
                sb.append(rs.getString("address"));
                sb.append(",");
                sb.append(rs.getString("id"));
                sb.append("\r\n");
            }

            pw.write(sb.toString());
            pw.close();
            System.out.println("csv genertion finished...");

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));

            message.setSubject("Testing Subject");

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText("This is message body");

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            String filename = "E:\\MoMagic\\DEMO.csv";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            //Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}