//NOSONAR
package org.seekay.contract.server.servet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.seekay.contract.server.util.ResponseWriter.to;

public class HealthServlet extends HttpServlet {

    public static final String SERVER_RUNNING = "Server running";

    @Override
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        to(httpResponse).ok().write(SERVER_RUNNING);
    }

}
