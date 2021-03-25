package znbang.svn.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Component
public class SvnKit {
    private String userName;
    private char[] password;

    public SvnKit(@Value("${svn.user}") String userName, @Value("${svn.password}") String password) {
        this.userName = userName;
        this.password = password.toCharArray();
    }

    private SVNClientManager createClientManager() {
        ISVNAuthenticationManager manager = SVNWCUtil.createDefaultAuthenticationManager(userName, password);
        SVNClientManager clientManager = SVNClientManager.newInstance();
        clientManager.setAuthenticationManager(manager);

        return clientManager;
    }

    public long getRevision(String url) throws SVNException {
        SVNClientManager clientManager = createClientManager();
        try {
            SVNRepository repos = clientManager.createRepository(SVNURL.parseURIEncoded(url), true);
            SVNDirEntry dirEntry = repos.getDir("", -1, false, null);
            return dirEntry.getRevision();
        } finally {
            clientManager.dispose();
        }
    }

    public String diff(String url, long startRevision, long endRevision) throws SVNException {
        SVNClientManager clientManager = createClientManager();
        SVNDiffClient diffClient = clientManager.getDiffClient();
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        try {
            diffClient.doDiff(SVNURL.parseURIEncoded(url), SVNRevision.UNDEFINED, SVNRevision.create(startRevision), SVNRevision.create(endRevision), SVNDepth.UNKNOWN, false, buf);
            return buf.toString(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return buf.toString();
        } finally {
            clientManager.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<SVNLogEntry> log(String url, long startRevision, long endRevision) throws SVNException {
        SVNClientManager clientManager = createClientManager();
        try {
            SVNRepository repos = clientManager.createRepository(SVNURL.parseURIEncoded(url), true);
            return repos.log(null, null, startRevision, endRevision, false, false);
        } finally {
            clientManager.dispose();
        }
    }
}
