export class Clazz {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	studentCount: number = undefined;
	students: Array<Student> = [];
}

export class Course {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	description: string = undefined;
	difficulty: string = undefined;
	categoryId: number = undefined;
	questionCount: number = undefined;
	coursewareCount: number = undefined;
	videoCount: number = undefined;
	category: CourseCategory = new CourseCategory();
	questions: Array<Question> = [];
	coursewares: Array<Courseware> = [];
	videos: Array<Video> = [];
}

export class CourseCategory {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	level: number = undefined;
	parentId: number = undefined;
	parent: CourseCategory = undefined;
	children: Array<CourseCategory> = [];
}

export class Courseware {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	courseId: number = undefined;
	fileId: string = undefined;
	fileName: string = undefined;
	size: number = undefined;
	ext: string = undefined;
}

export class Question {
	id: number = undefined;
	crTime: string = undefined;
	courseId: number = undefined;
	title: string = undefined;
	score: number = undefined;
	answer: string = undefined;
	ty: string = undefined;
	sc: QuestionChoice = new QuestionChoice();
}

export class QuestionChoice {
	id: number = undefined;
	crTime: string = undefined;
	a: string = undefined;
	b: string = undefined;
	c: string = undefined;
	d: string = undefined;
}

export class Student {
	id: number = undefined;
	crTime: string = undefined;
	loginName: string = undefined;
	userName: string = undefined;
	mobile: string = undefined;
	password: string = undefined;
	email: string = undefined;
	avatar: string = undefined;
	clazzId: number = undefined;
	clazz: Clazz = new Clazz();
	jobs: Array<StudentStudyJob> = [];
}

export class StudentStudyJob {
	id: number = undefined;
	crTime: string = undefined;
	jobId: number = undefined;
	studentId: number = undefined;
	status: string = undefined;
	job: StudyJob = new StudyJob();
	student: Student = new Student();
	items: Array<StudentStudyJobItem> = [];
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
	courseId: number = undefined;
	clazzId: number = undefined;
	limitDate: string = undefined;
	course: Course = new Course();
	clazz: Clazz = new Clazz();
	jobs: Array<StudentStudyJob> = [];
}

export class Team {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	students: Array<Student> = [];
}

export class Video {
	id: number = undefined;
	crTime: string = undefined;
	name: string = undefined;
	courseId: number = undefined;
	fileId: string = undefined;
	fileName: string = undefined;
	size: number = undefined;
	ext: string = undefined;
}