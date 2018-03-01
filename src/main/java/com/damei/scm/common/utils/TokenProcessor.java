package com.damei.scm.common.utils;

import com.damei.scm.common.Constants;
import org.apache.shiro.session.Session;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author zhangmin
 */
public final class TokenProcessor {

    private static TokenProcessor instance = new TokenProcessor();
    /**
     * The timestamp used most recently to generate a token value.
     */
    private long previous;

    protected TokenProcessor() {
        super();
    }

    public static TokenProcessor getInstance() {
        return instance;
    }

    public synchronized boolean isTokenValid(Session session, HttpServletRequest request) {
        return this.isTokenValid(session, request, false);
    }

    /**
     * Return <code>true</code> if there is a transaction token stored in the user's current session, and the value
     * submitted as a request parameter with this action matches it. Returns <code>false</code>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the transaction token in the user's session</li>
     * </ul>
     *
     * @param request The servlet request we are processing
     * @param reset   Should we reset the token after checking it?
     */
    public synchronized boolean isTokenValid(Session session, HttpServletRequest request, boolean reset) {
        // Retrieve the current session for this request
        if (session == null) {
            return false;
        }

        // Retrieve the transaction token from this session, and
        // reset it if requested
        String saved = (String) session.getAttribute(Constants.TRANSACTION_TOKEN_KEY);
        if (saved == null) {
            return false;
        }

        if (reset) {
            this.resetToken(session);
        }

        // Retrieve the transaction token included in this request
        String token = request.getParameter(Constants.TOKEN_KEY);
        if (token == null) {
            return false;
        }

        return saved.equals(token);
    }

    /**
     * Reset the saved transaction token in the user's session. This indicates that transactional token checking will
     * not be needed on the next request that is submitted.
     *
     * @param request The servlet request we are processing
     */
    public synchronized void resetToken(Session session) {
        if (session == null) {
            return;
        }
        session.removeAttribute(Constants.TRANSACTION_TOKEN_KEY);
    }

    /**
     * Save a new transaction token in the user's current session, creating a new session if necessary.
     *
     * @param request The servlet request we are processing
     */
    public synchronized void saveToken(Session session) {
        String token = generateToken(session);
        if (token != null) {
            session.setAttribute(Constants.TRANSACTION_TOKEN_KEY, token);
        }

    }

    /**
     * Generate a new transaction token, to be used for enforcing a single request for a particular transaction.
     *
     * @param request The request we are processing
     */
    private synchronized String generateToken(Session session) {
        try {
            String sessionId = (String) session.getId();
            byte id[] = sessionId.getBytes();
            long current = System.currentTimeMillis();
            if (current == previous) {
                current++;
            }
            previous = current;
            byte now[] = new Long(current).toString().getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

    }

    /**
     * Convert a byte array to a String of hexadecimal digits and return it.
     *
     * @param buffer The byte array to be converted
     */
    private String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
        }
        return sb.toString();
    }

}