package stashpullrequestbuilder.stashpullrequestbuilder.stash;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author blaffoy
 */
public class StashPullRequestBuildHistory implements Serializable {
    private static final Logger logger = Logger.getLogger(StashPullRequestBuildHistory.class.getName());

    private final ArrayList<HistoryItem> history;

    private class HistoryItem {
        public final String branch;
        public final String target;
        public final Integer commentId;

        public HistoryItem(String branch, String target, Integer commentId) {
            this.branch = branch;
            this.target = target;
            this.commentId = commentId;
        }

        @Override
        public String toString() {
            return "branch: \"" + branch + " ; target: \"" + target + "\"" +
                    "commentId: \"" + commentId + "\"";
        }

        public boolean equals(HistoryItem that) {
            boolean ret = this.branch.equals(that.branch) &&
                    this.target.equals(that.target) &&
                    this.commentId.equals(that.commentId);
            logger.log(Level.INFO, "Matching {0} against {1}", new Object[]{this, that});
            logger.log(Level.INFO, "Match: {0}", ret);
            return ret;
        }
    }

    public StashPullRequestBuildHistory() {
        logger.log(Level.INFO, "Setting up new Build History");
        this.history = new ArrayList<HistoryItem>();
    }

    public boolean hasBeenBuilt(String branchSha, String targetSha,
            Integer commentId) {
        return getLastBuild(branchSha, targetSha, commentId) != null;
    }

    public void save(String branchSha, String targetSha, Integer commentId) {
        history.add(new HistoryItem(branchSha, targetSha, commentId));
    }

    private HistoryItem getLastBuild(String branchSha, String targetSha,
            Integer commentId) {
        HistoryItem candidate = new HistoryItem(branchSha, targetSha, commentId);
        try {
            for(HistoryItem hi : history) {
                if(hi.equals(candidate)) {
                    return hi;
                }
            }

            return null;
        } catch(Exception ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(HistoryItem h: history) {
            sb.append(h.toString());
        }

        return sb.toString();
    }
}