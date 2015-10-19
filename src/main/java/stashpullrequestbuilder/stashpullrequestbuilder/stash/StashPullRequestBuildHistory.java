package stashpullrequestbuilder.stashpullrequestbuilder.stash;

import java.io.Serializable;
import java.util.HashSet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author blaffoy
 */
public class StashPullRequestBuildHistory implements Serializable {
    private static final Logger logger = Logger.getLogger(StashPullRequestBuildHistory.class.getName());

    private final HashSet<Merge> mergeTriggerHistory;

    private final HashSet<Integer> commentTriggerHistory;

    private class Merge {
        public final String branch;
        public final String target;

        public Merge(String branch, String target) {
            this.branch = branch;
            this.target = target;
        }

        @Override
        public String toString() {
            return "branch: \"" + branch + " ; target: \"" + target + "\"";
        }

        public boolean equals(Merge that) {
            boolean ret = this.branch.equals(that.branch) &&
                    this.target.equals(that.target);
            logger.log(Level.INFO, "Matching {0} against {1}", new Object[]{this, that});
            logger.log(Level.INFO, "Match: {0}", ret);
            return ret;
        }
    }

    public StashPullRequestBuildHistory() {
        logger.log(Level.INFO, "Setting up new Build History");
        this.mergeTriggerHistory = new HashSet<Merge>();
        this.commentTriggerHistory = new HashSet<Integer>();
    }

    public void saveMergeTrigger(String branchSha, String targetSha) {
        Merge m = new Merge(branchSha, targetSha);
        if (mergeHasBeenBuilt(m)) {
            logger.log(Level.SEVERE, "Merge trigger history already contains", m);
        } else {
            mergeTriggerHistory.add(m);
        }
    }

    private boolean mergeHasBeenBuilt(Merge m) {
        return mergeTriggerHistory.contains(m);
    }

    public boolean mergeHasBeenBuilt(String branchSha, String targetSha) {
        return mergeTriggerHistory.contains(new Merge(branchSha, targetSha));
    }

    public void saveCommentTrigger(Integer commentId) {
        if (commentHasBeenBuilt(commentId)) {
            logger.log(Level.SEVERE, "Comment trigger history already contains", commentId);
        } else {
            commentTriggerHistory.add(commentId);
        }
    }

    public boolean commentHasBeenBuilt(Integer commentId) {
        return commentTriggerHistory.contains(commentId);
    }
}