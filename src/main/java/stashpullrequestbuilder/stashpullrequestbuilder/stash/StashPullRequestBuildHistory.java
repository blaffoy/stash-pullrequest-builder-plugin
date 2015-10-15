package stashpullrequestbuilder.stashpullrequestbuilder.stash;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author blaffoy
 */
public class StashPullRequestBuildHistory implements Serializable {
    private class HistoryItem {
        public final String branch;
        public final String target;

        public HistoryItem(String branch, String target) {
            this.branch = branch;
            this.target = target;
        }

@Override
public String toString() {
            return "branch: \"" + branch + " ; target: \"" + target + "\"";
}
    }

    private final ArrayList<HistoryItem> history;

    public StashPullRequestBuildHistory() {
        history = new ArrayList<HistoryItem>();
    }

    public boolean hasBeenBuilt(String branchSha, String targetSha) {
        return getLastBuild(branchSha, targetSha) != null;
    }

    public void save(String branchSha, String targetSha) {
        history.add(new HistoryItem(branchSha, targetSha));
    }

    private HistoryItem getLastBuild(String branchSha, String targetSha) {
        try {
            for(HistoryItem hi : history) {
                if(hi.branch.equals(branchSha) && hi.target.equals(targetSha)) {
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