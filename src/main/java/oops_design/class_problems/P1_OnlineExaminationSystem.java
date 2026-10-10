import java.util.*;

// Online Examination System — OOP design with polymorphism + state guard.
// Classes / relationships / multiplicities:
//   Student (1) ---- (*) Attempt            : a student may have many attempts, each attempt belongs to one student
//   Examination (1) ---- (*) Question      : an examination owns several questions (composition)
//   Attempt (*) ---- (1) Examination        : each attempt targets exactly one examination
//   Question <|-- MCQQuestion, TrueFalseQuestion (inheritance; new types plug in via the abstract Question)
//   Attempt state: IN_PROGRESS -> SUBMITTED (one-way; answers frozen after submit)
// Evaluation is polymorphic: Attempt.submit() calls question.isCorrect(answer) without knowing the
// concrete question type, so new question types need no change in the submission/evaluation workflow.
// Object interactions: Student -> ExaminationService.startExam() -> creates Attempt ->
// Attempt.answerQuestion() (records answers) -> ExaminationService.submitAttempt() ->
// Attempt.submit() evaluates each answer via Question.isCorrect() and reports the score.

enum AttemptStatus {
    IN_PROGRESS, SUBMITTED
}

abstract class Question {
    private final int id;
    private final String text;

    Question(int id, String text) {
        this.id = id;
        this.text = text;
    }

    int getId() {
        return id;
    }

    String getText() {
        return text;
    }

    abstract boolean isCorrect(String answer);

    abstract String getCorrectAnswer();
}

class MCQQuestion extends Question {
    private final String correctOption;

    MCQQuestion(int id, String text, String correctOption) {
        super(id, text);
        this.correctOption = correctOption;
    }

    @Override
    boolean isCorrect(String answer) {
        return answer != null && answer.trim().equalsIgnoreCase(correctOption);
    }

    @Override
    String getCorrectAnswer() {
        return correctOption;
    }
}

class TrueFalseQuestion extends Question {
    private final boolean correctValue;

    TrueFalseQuestion(int id, String text, boolean correctValue) {
        super(id, text);
        this.correctValue = correctValue;
    }

    @Override
    boolean isCorrect(String answer) {
        if (answer == null) {
            return false;
        }
        String normalized = answer.trim().toLowerCase();
        boolean given = normalized.equals("true") || normalized.equals("t");
        return given == correctValue;
    }

    @Override
    String getCorrectAnswer() {
        return String.valueOf(correctValue);
    }
}

class Examination {
    private final String name;
    private final List<Question> questions = new ArrayList<>();

    Examination(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void addQuestion(Question question) {
        questions.add(question);
    }

    Question getQuestionById(int id) {
        for (Question question : questions) {
            if (question.getId() == id) {
                return question;
            }
        }
        return null;
    }

    List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}

class Student {
    private final String name;

    Student(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Attempt {
    private final Student student;
    private final Examination examination;
    private final Map<Integer, String> answers = new LinkedHashMap<>();
    private AttemptStatus status = AttemptStatus.IN_PROGRESS;
    private int correctCount;

    Attempt(Student student, Examination examination) {
        this.student = student;
        this.examination = examination;
    }

    Student getStudent() {
        return student;
    }

    Examination getExamination() {
        return examination;
    }

    AttemptStatus getStatus() {
        return status;
    }

    void answerQuestion(int questionId, String answer) {
        if (status == AttemptStatus.SUBMITTED) {
            System.out.println("Cannot update answer: attempt for '" + examination.getName() + "' is already submitted.");
            return;
        }
        Question question = examination.getQuestionById(questionId);
        if (question == null) {
            System.out.println("Question " + questionId + " does not belong to '" + examination.getName() + "'.");
            return;
        }
        answers.put(questionId, answer);
        System.out.println("Question " + questionId + " answered with '" + answer + "'.");
    }

    void submit() {
        if (status == AttemptStatus.SUBMITTED) {
            System.out.println("Examination '" + examination.getName() + "' is already submitted.");
            return;
        }
        correctCount = 0;
        for (Question question : examination.getQuestions()) {
            if (question.isCorrect(answers.get(question.getId()))) {
                correctCount++;
            }
        }
        status = AttemptStatus.SUBMITTED;
        System.out.println("Examination '" + examination.getName() + "' submitted successfully. Result for '"
                + examination.getName() + "' attempt: " + correctCount + "/" + examination.getQuestions().size()
                + " correct.");
    }
}

class ExaminationService {
    private final Set<String> submittedKeys = new HashSet<>();

    Attempt startExam(Student student, Examination examination) {
        String key = student.getName() + "|" + examination.getName();
        if (submittedKeys.contains(key)) {
            System.out.println("Examination '" + examination.getName() + "' already has a submitted attempt for "
                    + student.getName() + ".");
            return null;
        }
        System.out.println("Examination '" + examination.getName() + "' started by " + student.getName() + ".");
        return new Attempt(student, examination);
    }

    void submitAttempt(Attempt attempt) {
        if (attempt == null) {
            return;
        }
        attempt.submit();
        if (attempt.getStatus() == AttemptStatus.SUBMITTED) {
            submittedKeys.add(attempt.getStudent().getName() + "|" + attempt.getExamination().getName());
        }
    }
}

public class P1_OnlineExaminationSystem {
    public static void main(String[] args) {
        Examination mathQuiz = new Examination("Math Quiz");
        mathQuiz.addQuestion(new MCQQuestion(1, "What is 2 + 2?", "A"));
        mathQuiz.addQuestion(new MCQQuestion(2, "What is 5 * 3?", "B"));
        // New types (e.g. TrueFalseQuestion above) evaluate through the same Attempt.submit()
        // workflow with zero changes to the submission/evaluation code.

        Student student = new Student("Aarav");
        ExaminationService service = new ExaminationService();

        Attempt attempt = service.startExam(student, mathQuiz);
        attempt.answerQuestion(1, "A");
        attempt.answerQuestion(2, "C");
        service.submitAttempt(attempt);

        // Answers are frozen after submission:
        attempt.answerQuestion(1, "B");
    }
}
