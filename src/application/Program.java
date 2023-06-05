package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;

public class Program {

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Connection conn = null; // Cria��o da vari�vel conn do tipo Connection.
		PreparedStatement st = null; // Cria��o da vari�vel st do tipo PreparedStatement.
		try {
			conn = DB.getConnection(); // .getConnection() conecta com o BD.

			st = conn.prepareStatement( // Pega o comando sql dentro dos par�ntes e retorna o resultado para um objeto PreparedStatement.
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS); // O prepareStatement() permite o uso de placeholders (?) (espa�os reservados).
					// Statement.RETURN_GENERATED_KEYS recupera as chaves geradas automaticamente ap�s a execu��o de uma instru��o SQL de inser��o.

			st.setString(1, "Carl Purple"); //.set+tipoDoCampo (coluna) chamado a partir de um objeto PreparedStatement permite inserir dados no BD.
			st.setString(2, "carl@gmail.com");
			st.setDate(3, new java.sql.Date(sdf.parse("22/04/1985").getTime())); // Aten��o para a inser��o de tipos Date no BD.
			st.setDouble(4, 3000.0);
			st.setInt(5, 4); // Primeiro campo antes da v�rgula � o placeholder e o segundo campo � o valor que ter� nesse espa�o. 
			
			
			// EXAMPLE 2: Demonstra��o de inser��o de duas linhas em um comando somente.
			// st = conn.prepareStatement(
			//		"insert into department (Name) values ('D1'),('D2')", 
			//		Statement.RETURN_GENERATED_KEYS);

			int rowsAffected = st.executeUpdate(); // .executeUpdate() executa o comando sql armazenado em st e retorna o n� de linhas afetadas.
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys(); // .getGeneratedKeys() vai retornar as chaves geradas automaticamente ap�s o .executeUpdate().
				while (rs.next()) { // Enquanto o pr�ximo valor de rs seja true.
					int id = rs.getInt(1); // A vari�vel id recebe o valor da primeira coluna, que � do tipo int, armazenada em rs.
					System.out.println("Done! Id = " + id);
				}
			}
			else {
				System.out.println("No rows affected.");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(st); // chamada do m�todo que fecha o Statement.
			DB.closeConnection(); // chamada do m�todo que fecha o Connection.
		}
	}
}