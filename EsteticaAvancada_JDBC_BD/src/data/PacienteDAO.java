/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author crist
 */
public class PacienteDAO {
        
    public Connection conn; 
    public PreparedStatement st; 
    public ResultSet rs; 
    
    private String url = "jdbc:mysql://localhost:3306/PI_Mod2_esteticaAvancada"; 
    private String user = "root"; 
    private String password = "!753SqL951"; 
    
    public boolean conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão realizada com sucesso");
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Falha na conexão com o banco " + ex.getMessage());
            return false;
        }
    }
    
    public int salvar(Paciente pac) {
        
        int status;
        
        try {
            st = conn.prepareStatement("INSERT INTO pacientes (nome, cpf, telefone, endereco, convenio) VALUES (?,?,?,?,?)");
            st.setString(1, pac.getNome());
            st.setString(2, pac.getCpf());
            st.setString(3, pac.getTelefone());
            st.setString(4, pac.getEndereco());
            st.setString(5, pac.getConvenio());
            
            status = st.executeUpdate();
            return status; 
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar: " + ex.getMessage());
            return ex.getErrorCode();
        } finally {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao fechar o PreparedStatement: " + ex.getMessage());
        }
        }
    }
    
    public Paciente consulta (int id) {
        
        try {
            Paciente pacienteEncontrado = new Paciente();
            st = conn.prepareStatement("SELECT * FROM pacientes WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if(rs.next()) {
                pacienteEncontrado.setNome(rs.getString("nome"));
                pacienteEncontrado.setCpf(rs.getString("cpf"));
                pacienteEncontrado.setTelefone(rs.getString("telefone"));
                pacienteEncontrado.setEndereco(rs.getString("endereco"));
                pacienteEncontrado.setConvenio(rs.getString("convenio"));
                return pacienteEncontrado;
            } else {
                return null;
            }
        } catch (SQLException ex){
            System.out.println("Erro de consulta" + ex.getMessage());
            return null;
                }
        }

    
    public Paciente consultaPorNome(String nome) {
    try {
        Paciente pacienteEncontrado = new Paciente();
        st = conn.prepareStatement("SELECT * FROM pacientes WHERE nome = ?");
        st.setString(1, nome);
        rs = st.executeQuery();
        
        if(rs.next()) {
            pacienteEncontrado.setId(rs.getInt("id"));
            pacienteEncontrado.setNome(rs.getString("nome"));
            pacienteEncontrado.setCpf(rs.getString("cpf"));
            pacienteEncontrado.setTelefone(rs.getString("telefone"));
            pacienteEncontrado.setEndereco(rs.getString("endereco"));
            pacienteEncontrado.setConvenio(rs.getString("convenio"));
            return pacienteEncontrado;
        } else {
            return null;
        }
    } catch (SQLException ex) {
        System.out.println("Erro de consulta: " + ex.getMessage());
        return null;
    }
}
    
    public boolean atualizarPaciente (Paciente paciente) {
        
         try {
        if (consulta(paciente.getId()) != null) {
            st = conn.prepareStatement("UPDATE pacientes SET nome = ?, cpf = ?, telefone = ?, endereco = ?, convenio = ? WHERE id = ?");
            st.setString(1, paciente.getNome());
            st.setString(2, paciente.getCpf());
            st.setString(3, paciente.getTelefone());
            st.setString(4, paciente.getEndereco());
            st.setString(5, paciente.getConvenio());
            st.setInt(6, paciente.getId());
            int status = st.executeUpdate();
            return status > 0;
        } else {
            System.out.println("Paciente não encontrado.");
            return false;
        }
    } catch (SQLException ex) {
        System.out.println("Não foi possível atualizar os dados: " + ex.getMessage());
        return false;
    }
        
        
        }
    
    public List<Paciente> listagem (String termoBusca) {
        
        try {
            List<Paciente> lista = new ArrayList<>();
            
            String sqlFiltro = "SELECT * FROM pacientes";
            
             if (!termoBusca.isEmpty()) {
                sqlFiltro = sqlFiltro + " WHERE nome like ?";                
            }
            st = conn.prepareStatement(sqlFiltro);
            if (!termoBusca.isEmpty()) {
                st.setString(1, "%" + termoBusca + "%");
            }
            rs = st.executeQuery();
            while(rs.next()) {
                Paciente pacienteEncontrado = new Paciente();
                pacienteEncontrado.setNome(rs.getString("nome"));
                pacienteEncontrado.setCpf(rs.getString("cpf"));
                pacienteEncontrado.setTelefone(rs.getString("telefone"));
                pacienteEncontrado.setEndereco(rs.getString("endereco"));
                pacienteEncontrado.setConvenio(rs.getString("convenio"));
                lista.add(pacienteEncontrado);
            }
            return lista;
            
        } catch (SQLException ex){
            System.out.println("Erro de consulta" + ex.getMessage());
            return null;
        } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao fechar a conexão" + ex.getMessage());
        }
    }
    }
  
    public boolean excluirPacientePorNome(String nome) {
    try {
        if(conn == null || !conn.isValid(5)) {
            conectar();
        }
        st = conn.prepareStatement("DELETE FROM pacientes WHERE nome = ?");
        st.setString(1, nome);
        int rowsDeleted = st.executeUpdate();
        return rowsDeleted > 0;     
    } catch (SQLException ex) {
        System.out.println("Não foi possível excluir o paciente: " + ex.getMessage());
        return false;
    } finally {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao fechar o PreparedStatement: " + ex.getMessage());
        }
    }
}
    public boolean excluirPacientePorID(int id) {
    try {
        if(conn == null || !conn.isValid(5)) {
            conectar();
        }
        st = conn.prepareStatement("DELETE FROM pacientes WHERE id = ?");
        st.setInt(1, id);
        int rowsDeleted = st.executeUpdate();
        return rowsDeleted > 0;     
    } catch (SQLException ex) {
        System.out.println("Não foi possível excluir o paciente: " + ex.getMessage());
        return false;
    } finally {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao fechar o PreparedStatement: " + ex.getMessage());
        }
    }
}
    public void desconectar() {
       
        try {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("Conexão fechada com sucesso");
        }
    } catch (SQLException ex) {
        System.out.println("Erro ao desconectar: " + ex.getMessage());
    }
    }
    
    
}

                