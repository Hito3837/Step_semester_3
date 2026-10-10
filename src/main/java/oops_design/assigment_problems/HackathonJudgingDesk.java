import java.util.*;

// The Code Sprint Judging Desk — hackathon management system.
// Classes / relationships / multiplicities:
//   Hackathon (1) ---- (*) HackTeam        : a hackathon hosts many teams, each team belongs to one hackathon
//   HackTeam (1) ---- (2..4) HackStudent   : team-size invariant enforced at registration
//   HackStudent (*) ---- (0..1) HackTeam   : a student belongs to at most one team per hackathon
//   HackTeam (1) ---- (0..1) HackProject   : a team may submit at most one project
//   ScoringPolicy <|-- InnovationScoring, OpenScoring (interface; new tracks plug in polymorphically)
//   Hackathon stage: OPEN -> JUDGING -> PUBLISHED (one-way; published results are locked)
// Object interactions: Organiser -> Hackathon.registerTeam() (validates size + single-team rules) ->
// HackTeam.submitProject() -> HackathonJudge.scoreProject() (delegates final score to the team's
// ScoringPolicy, so new tracks need no change in registration/submission/publishing) ->
// Hackathon.publishResults() locks every score.

enum HackathonStage {
    OPEN, JUDGING, PUBLISHED
}

interface ScoringPolicy {
    double finalScore(int idea, int execution, int presentation);
}

class InnovationScoring implements ScoringPolicy {
    @Override
    public double finalScore(int idea, int execution, int presentation) {
        return idea * 0.5 + execution * 0.3 + presentation * 0.2;
    }
}

class OpenScoring implements ScoringPolicy {
    @Override
    public double finalScore(int idea, int execution, int presentation) {
        return (idea + execution + presentation) / 3.0;
    }
}

class HackStudent {
    private final String name;

    HackStudent(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class HackProject {
    private final String title;
    private final HackTeam team;
    private Integer idea;
    private Integer execution;
    private Integer presentation;

    HackProject(String title, HackTeam team) {
        this.title = title;
        this.team = team;
    }

    String getTitle() {
        return title;
    }

    HackTeam getTeam() {
        return team;
    }

    boolean isScored() {
        return idea != null;
    }

    void recordScores(int idea, int execution, int presentation) {
        this.idea = idea;
        this.execution = execution;
        this.presentation = presentation;
    }

    double finalScore() {
        return team.getScoringPolicy().finalScore(idea, execution, presentation);
    }
}

class HackTeam {
    private final String name;
    private final List<HackStudent> members;
    private final String trackName;
    private final ScoringPolicy scoringPolicy;
    private HackProject project;

    HackTeam(String name, List<HackStudent> members, String trackName, ScoringPolicy scoringPolicy) {
        this.name = name;
        this.members = members;
        this.trackName = trackName;
        this.scoringPolicy = scoringPolicy;
    }

    String getName() {
        return name;
    }

    List<HackStudent> getMembers() {
        return members;
    }

    String getTrackName() {
        return trackName;
    }

    ScoringPolicy getScoringPolicy() {
        return scoringPolicy;
    }

    HackProject submitProject(String title, HackathonStage stage) {
        if (stage == HackathonStage.PUBLISHED) {
            System.out.println("Submission rejected: Results have already been published.");
            return null;
        }
        if (project != null) {
            System.out.println("Submission failed: " + name + " has already submitted a project.");
            return null;
        }
        project = new HackProject(title, this);
        System.out.println("Project '" + title + "' submitted by " + name + ".");
        return project;
    }
}

class HackathonJudge {
    private final String name;

    HackathonJudge(String name) {
        this.name = name;
    }

    void scoreProject(HackProject project, int idea, int execution, int presentation,
            HackathonStage stage) {
        if (project == null) {
            return;
        }
        if (stage == HackathonStage.PUBLISHED) {
            System.out.println("Rescore rejected: Results have already been published.");
            return;
        }
        if (idea < 0 || idea > 10 || execution < 0 || execution > 10
                || presentation < 0 || presentation > 10) {
            System.out.println("Score rejected: ratings must each be between 0 and 10.");
            return;
        }
        project.recordScores(idea, execution, presentation);
        System.out.printf("Score recorded for '%s'. Final score: %.2f.%n",
                project.getTitle(), project.finalScore());
    }
}

class Hackathon {
    private final String name;
    private final List<HackTeam> teams = new ArrayList<>();
    private final Set<String> teamedStudents = new HashSet<>();
    private HackathonStage stage = HackathonStage.OPEN;

    Hackathon(String name) {
        this.name = name;
    }

    HackTeam registerTeam(String teamName, List<String> memberNames, String trackName,
            ScoringPolicy scoringPolicy) {
        if (memberNames.size() < 2 || memberNames.size() > 4) {
            System.out.println("Registration failed: A team must have 2 to 4 members.");
            return null;
        }
        for (String member : memberNames) {
            if (teamedStudents.contains(member)) {
                System.out.println("Registration failed: " + member + " already belongs to a team.");
                return null;
            }
        }
        List<HackStudent> members = new ArrayList<>();
        for (String member : memberNames) {
            members.add(new HackStudent(member));
            teamedStudents.add(member);
        }
        HackTeam team = new HackTeam(teamName, members, trackName, scoringPolicy);
        teams.add(team);
        System.out.println("Team " + teamName + " registered (" + members.size()
                + " members, " + trackName + " track).");
        return team;
    }

    void publishResults() {
        stage = HackathonStage.PUBLISHED;
        System.out.println("Results published.");
    }

    HackathonStage getStage() {
        return stage;
    }
}

public class HackathonJudgingDesk {
    public static void main(String[] args) {
        Hackathon codeSprint = new Hackathon("Code Sprint");
        HackathonJudge judge = new HackathonJudge("Meera");

        HackTeam byteBusters = codeSprint.registerTeam("ByteBusters",
                Arrays.asList("Asha", "Ravi", "Neha"), "Innovation", new InnovationScoring());
        codeSprint.registerTeam("SoloCoder",
                Collections.singletonList("Kiran"), "Open", new OpenScoring());

        HackProject smartAttend = byteBusters.submitProject("SmartAttend", codeSprint.getStage());
        judge.scoreProject(smartAttend, 8, 7, 9, codeSprint.getStage());
        codeSprint.publishResults();
        judge.scoreProject(smartAttend, 10, 7, 9, codeSprint.getStage());
    }
}
