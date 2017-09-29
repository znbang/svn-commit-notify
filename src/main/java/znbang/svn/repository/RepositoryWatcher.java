package znbang.svn.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNLogEntry;
import znbang.svn.common.Mailer;
import znbang.svn.common.SvnKit;
import znbang.svn.user.User;
import znbang.svn.user.UserStore;

import java.io.IOException;
import java.util.Collection;

/**
 * 每分鐘檢查repository是否有新的commit。
 */
@Component
public class RepositoryWatcher {
    private final Logger LOG = LoggerFactory.getLogger(RepositoryWatcher.class);
    private SvnKit svnKit;
    private RepositoryStore repositoryStore;
    private UserStore userStore;
    private Mailer mailer;
    private MessageSource messageSource;
    private String sender;

    public RepositoryWatcher(RepositoryStore repositoryStore, UserStore userStore, Mailer mailer,
                             MessageSource messageSource, SvnKit svnKit,
                             @Value("${mail.sender}") String sender) {
        this.repositoryStore = repositoryStore;
        this.userStore = userStore;
        this.mailer = mailer;
        this.messageSource = messageSource;
        this.svnKit = svnKit;
        this.sender = sender;
    }

    @Scheduled(fixedRate = 60000)
    public void check() throws Exception {
        for (Repository a : repositoryStore.list()) {
            check(a);
        }
    }

    private void check(Repository repos) throws Exception {
        // 檢查是否有新的revision
        long startRevision = repos.getRevision();
        long endRevision = svnKit.getRevision(repos.getUrl());
        if (endRevision > startRevision) {
            // 用新、舊revision取得commit清單，為每個commit產生diff寄給使用者
            Collection<SVNLogEntry> logs = svnKit.log(repos.getUrl(), startRevision, endRevision);
            SVNLogEntry prevLog = null;
            for (SVNLogEntry log : logs) {
                if (prevLog == null) {
                    prevLog = log;
                } else {
                    try {
                        String diff = svnKit.diff(repos.getUrl(), prevLog.getRevision(), log.getRevision());
                        mail(repos.getUrl(), log, diff);
                    } catch (Throwable e) {
                        LOG.error("diff failed, rev1: {}, rev2: {}, exception: {}", prevLog.getRevision(), log.getRevision(), e);
                    }
                    prevLog = log;
                }
            }
            // 儲存新的revision
            repositoryStore.save(new Repository(repos.getUrl(), endRevision));
        }
    }

    private void mail(String url, SVNLogEntry log, String diff) throws IOException {
        String subject = messageSource.getMessage("mail.subject", null, null);
        String text = "URL: " + url + "\n" +
                "Date: " + log.getDate() + "\n" +
                "Author: " + log.getAuthor() + "\n" +
                "Revision: " + log.getRevision() + "\n\n" +
                log.getMessage() + "\n\n" +
                diff;
        for (User user : userStore.list()) {
            mailer.send(sender, user.getEmail(), subject, text);
        }
    }
}
