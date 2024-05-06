package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sha256.SHA256;

@WebServlet("/Login")
public class Login extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public Login() {
      super();
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
                                              throws ServletException, IOException {
      
	  request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");

      String tid = request.getParameter("tid");
      String password = request.getParameter("pw");

      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      ResultSet rs = null;  
      int mileage = 0;
      String salt = null;       
      String hashedPassword = null;

      try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
         Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");

         String sql = "SELECT pw, salt, mileage FROM tmember WHERE tid = ?";
         PreparedStatement pstmt = con.prepareStatement(sql);
         pstmt.setString(1, tid);
         rs = pstmt.executeQuery();
            
         if (rs.next()) {
                salt = rs.getString("salt");
                mileage = rs.getInt("mileage");
                hashedPassword = SHA256.encrypt(password, salt);
             
                // 비밀번호 일치 여부 확인
                if (hashedPassword.equals(rs.getString("pw"))) {
                    // 로그인 성공
                   
                    session.setAttribute("sid", tid);
                    session.setAttribute("mileage", mileage);
                    response.sendRedirect("00_main.jsp");
                } else {
                    // 비밀번호 불일치
                  out.println("<script>");
                   out.println("alert('일치하는정보가 없습니다.');");
                   out.println("window.location.href = '01_Login.jsp';");
                   out.println("</script>");
                }
            } else {
                  // 해당 아이디가 없음
                 out.println("<script>");
                 out.println("alert('일치하는정보가 없습니다.');");
                 out.println("window.location.href = '01_Login.jsp';");
                 out.println("</script>");
            }

         rs.close();
         pstmt.close();
         con.close();
      } catch (Exception e) {

         out.println("<script>");
          out.println("alert('일치하는정보가 없습니다.');");
          out.println("window.location.href = '01_Login.jsp;");
          out.println("</script>");
          //먼 훗날의 나에세: 
          //열정이 사그라들때면 이코드를 보고 다시금 열정으로 타오르기를
          //나도 이렇게 어리숙하게 코드를 짰을때가 있었다는 사실을
          //열정과 채찍이 되길
      }
   }
}