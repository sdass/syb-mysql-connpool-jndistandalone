import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateByMain {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.sybase.jdbc4.jdbc.SybDriver";
	static final String DB_URL = "jdbc:sybase:Tds:somesybdb01.abc.corp:5000/common_db";

	// Database credentials
	static final String USER = "uuuuu";
	static final String PASS = "xxxxx";

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		String oldPassword = "Anew1234";
		String newPassword = "ssy123456";
		String email = "ssy@ky.com"; //email field is case insensitive
		Long customerId = 1662680L;

		CredentialModel updateCandidate = new CredentialModel(oldPassword,
				newPassword, email, customerId);

		new UpdateByMain().updateRecord(updateCandidate);
	}

	public void updateRecord(CredentialModel o) throws ClassNotFoundException,
			SQLException {
		System.out.println("This is atest 2");
		System.out.println(o);

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName(JDBC_DRIVER); // driver loaded
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println(connection != null);

			preparedStatement = connection
					.prepareStatement("UPDATE prod.prod_customers   SET password = ? WHERE customer_id = ? and email=? and password= ?");

			preparedStatement.setString(1, o.newPassword);
			preparedStatement.setLong(2, o.customerId);
			preparedStatement.setString(3, o.email);
			preparedStatement.setString(4, o.oldPassword);

			int count = preparedStatement.executeUpdate();
			System.out.println("count=" + count); //-- count = 1 password update successful

		} catch (SQLException e) {
			throw e;
		} finally {
			preparedStatement.close();
			connection.close();

		}

	}

}

// --------------------------
class CredentialModel {
	String oldPassword;
	String newPassword;
	String email;
	Long customerId;

	public CredentialModel(String oldPassword, String newPassword,
			String email, Long customerId) {
		super();
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.email = email;
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "CredentialModel [oldPassword=" + oldPassword + ", newPassword="
				+ newPassword + ", email=" + email + ", customerId="
				+ customerId + "]";
	}

}
