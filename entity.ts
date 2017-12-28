export class Category {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	level: number = undefined;
	parentId: number = undefined;
	ty: string = undefined;
	parent: Category = undefined;
	children: Category[] = [];
}

export class Clazz {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	studentCount: number = undefined;
	students: Student[] = [];
}

export class Course {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	description: string = undefined;
	difficulty: string = undefined;
	coursewares: CourseCourseware[] = [];
	videos: CourseVideo[] = [];
	questions: CourseQuestion[] = [];
	questionCount: number = undefined;
	coursewareCount: number = undefined;
	videoCount: number = undefined;
	cate0Id: number = undefined;
	cate0: Category = new Category();
	cate1Id: number = undefined;
	cate1: Category = new Category();
}

export class CourseCourseware {
	id: number = undefined;
	crTime: string = undefined;
	courseId: number = undefined;
	course: Course = new Course();
	coursewareId: number = undefined;
	courseware: Courseware = new Courseware();
}

export class CourseQuestion {
	id: number = undefined;
	crTime: string = undefined;
	courseId: number = undefined;
	course: Course = new Course();
	questionId: number = undefined;
	question: Question = new Question();
}

export class CourseVideo {
	id: number = undefined;
	crTime: string = undefined;
	courseId: number = undefined;
	course: Course = new Course();
	videoId: number = undefined;
	video: Video = new Video();
}

export class Courseware {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	fileId: string = undefined;
	fileName: string = undefined;
	size: number = undefined;
	ext: string = undefined;
	cate0Id: number = undefined;
	cate0: Category = new Category();
	cate1Id: number = undefined;
	cate1: Category = new Category();
}

export class Question {
	id: number = undefined;
	crTime: string = undefined;
	title: string = undefined;
	score: number = undefined;
	answer: string = undefined;
	ty: string = undefined;
	sc: QuestionChoice = new QuestionChoice();
	cate0Id: number = undefined;
	cate0: Category = new Category();
	cate1Id: number = undefined;
	cate1: Category = new Category();
}

export class QuestionChoice {
	id: number = undefined;
	crTime: string = undefined;
	a: string = undefined;
	b: string = undefined;
	c: string = undefined;
	d: string = undefined;
}

export class Quiz {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	limitDate: string = undefined;
	questions: QuizQuestion[] = [];
	clazz: Clazz = new Clazz();
	clazzId: number = undefined;
	jobs: QuizJob[] = [];
}

export class QuizJob {
	id: number = undefined;
	crTime: string = undefined;
	quiz: Quiz = new Quiz();
	quizId: number = undefined;
	student: Student = new Student();
	studentId: number = undefined;
	status: string = undefined;
	score: number = undefined;
	items: QuizJobItem[] = [];
	itemCount: number = undefined;
	finishCount: number = undefined;
	totalScore: number = undefined;
}

export class QuizJobItem {
	id: number = undefined;
	crTime: string = undefined;
	jobId: number = undefined;
	answer: string = undefined;
	score: number = undefined;
	correct: boolean = undefined;
	status: string = undefined;
	question: Question = new Question();
	questionId: number = undefined;
}

export class QuizQuestion {
	id: number = undefined;
	crTime: string = undefined;
	quiz: Quiz = new Quiz();
	quizId: number = undefined;
	question: Question = new Question();
	questionId: number = undefined;
}

export class Student {
	id: number = undefined;
	crTime: string = undefined;
	user: User = new User();
	name: string = undefined;
	mobile: string = undefined;
	password: string = undefined;
	email: string = undefined;
	avatar: string = undefined;
	clazz: Clazz = new Clazz();
	clazzId: number = undefined;
	team: TeamApply = new TeamApply();
	jobs: StudentStudyJob[] = [];
}

export class StudentStudyJob {
	id: number = undefined;
	crTime: string = undefined;
	job: StudyJob = new StudyJob();
	jobId: number = undefined;
	student: Student = new Student();
	studentId: number = undefined;
	status: string = undefined;
	items: StudentStudyJobItem[] = [];
}

export class StudentStudyJobItem {
	id: number = undefined;
	crTime: string = undefined;
	studentStudyJobId: number = undefined;
	targetId: number = undefined;
	ty: string = undefined;
	status: string = undefined;
	answer: string = undefined;
	correct: boolean = undefined;
	score: number = undefined;
}

export class StudyJob {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	course: Course = new Course();
	courseId: number = undefined;
	clazz: Clazz = new Clazz();
	clazzId: number = undefined;
	limitDate: string = undefined;
	jobs: StudentStudyJob[] = [];
}

export class Team {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	creater: Student = new Student();
	createrId: number = undefined;
	students: TeamApply[] = [];
	studentCount: number = undefined;
}

export class TeamApply {
	id: number = undefined;
	crTime: string = undefined;
	student: Student = undefined;
	studentId: number = undefined;
	team: Team = undefined;
	teamId: number = undefined;
	status: string = undefined;
}

export class User {
	id: number = undefined;
	crTime: string = undefined;
	username: string = undefined;
	password: string = undefined;
	role: string = undefined;
	ty: string = undefined;
}

export class Video {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	fileId: string = undefined;
	fileName: string = undefined;
	size: number = undefined;
	ext: string = undefined;
	cate0Id: number = undefined;
	cate0: Category = new Category();
	cate1Id: number = undefined;
	cate1: Category = new Category();
}