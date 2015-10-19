package stashpullrequestbuilder.stashpullrequestbuilder.stash;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Merge))
                return false;
            if (obj == this)
                return true;
            
            Merge rhs = (Merge) obj;
            boolean ret =  new EqualsBuilder().
                    append(branch, rhs.branch).
                    append(target, rhs.target).
                    isEquals();
            logger.log(Level.INFO, "Matching {0} against {1}", new Object[]{this, rhs});
            logger.log(Level.INFO, ".equals() = {0}", ret);
            return ret;
        }
        
        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 31).
                    append(branch).
                    append(target).
                    toHashCode();
        }
    }

    public StashPullRequestBuildHistory() {
        logger.log(Level.INFO, "Setting up new Build History");
        this.mergeTriggerHistory = new HashSet<Merge>();
        this.commentTriggerHistory = new HashSet<Integer>();
    }

    public void saveMergeTrigger(String branchSha, String targetSha) {
        Merge m = new Merge(branchSha, targetSha);
        String mth = mergeTriggerHistory.toString();
        if (mergeHasBeenBuilt(m)) {
            logger.log(Level.SEVERE, "Merge trigger history already contains", m);
        } else {
            logger.log(Level.INFO, "Adding {0} to {1}", new Object[]{m, mth});
            mergeTriggerHistory.add(m);
        }
    }

    private boolean mergeHasBeenBuilt(Merge m) {
        Merge foo = new Merge("foobar", "barfoo");
        logger.log(Level.INFO, "Checking apache commons equals builder: {0}", foo.equals(m));
        return mergeTriggerHistory.contains(m);
    }

    public boolean mergeHasBeenBuilt(String branchSha, String targetSha) {
        Merge m = new Merge(branchSha, targetSha);
        String mth = mergeTriggerHistory.toString();
        logger.log(Level.INFO, "Checking for {0} in {1}", new Object[]{m, mth});
        boolean ret = mergeTriggerHistory.contains(m);
        logger.log(Level.INFO, "Found in merge history?: {0}", ret);
        return ret;
    }

    public void saveCommentTrigger(Integer commentId) {
        String cth = commentTriggerHistory.toString();
        if (commentHasBeenBuilt(commentId)) {
            logger.log(Level.SEVERE, "Comment trigger history already contains", commentId);
        } else {
            logger.log(Level.INFO, "Adding {0} to {1}", new Object[]{commentId, cth});
            commentTriggerHistory.add(commentId);
        }
    }

    public boolean commentHasBeenBuilt(Integer commentId) {
        String cth = commentTriggerHistory.toString();
        logger.log(Level.INFO, "Checking for {0} in {1}", new Object[]{commentId, cth});
        boolean ret = commentTriggerHistory.contains(commentId);
        logger.log(Level.INFO, "Found in comment history?: {0}", ret);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Comment Trigger History:\n");
        sb.append(commentTriggerHistory.toString());
        sb.append("\n");
        sb.append("Merge Trigger History:\n");
        sb.append(mergeTriggerHistory.toString());
        sb.append("\n");

        return sb.toString();
    }
}