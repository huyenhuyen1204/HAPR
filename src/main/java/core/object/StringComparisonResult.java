package core.object;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.ArrayList;
import java.util.List;

public class StringComparisonResult {
    private boolean isEquals;
    private List<DiffMatchPatch.Diff> differentCharacters;

    public StringComparisonResult(boolean isEquals) {
        this.isEquals = isEquals;
        differentCharacters = new ArrayList<>();
    }

    public StringComparisonResult(List<DiffMatchPatch.Diff> differentCharacters) {
        if (differentCharacters.size() > 0) {
            this.isEquals = false;
            this.differentCharacters = differentCharacters;
        } else {
            this.isEquals = true;
        }
    }

    public void addDiffChar(DiffMatchPatch.Diff differentCharacter) {
        differentCharacters.add(differentCharacter);
    }

    public boolean isEquals() {
        return isEquals;
    }

    public void setEquals(boolean equals) {
        isEquals = equals;
    }

    public List<DiffMatchPatch.Diff> getDifferentCharacters() {
        return differentCharacters;
    }

    public void setDifferentCharacters(List<DiffMatchPatch.Diff> differentCharacters) {
        this.differentCharacters.addAll(differentCharacters);
    }
}
