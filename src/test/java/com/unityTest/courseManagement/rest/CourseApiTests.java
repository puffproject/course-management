package com.unityTest.courseManagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.unityTest.courseManagement.constants.ExceptionMsg;
import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.models.CourseAttributeName;
import com.unityTest.courseManagement.models.Term;
import com.unityTest.courseManagement.repository.CourseAttrRepository;
import com.unityTest.courseManagement.repository.CourseRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.unityTest.courseManagement.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements component API tests for Course API endpoints
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CourseApiTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseAttrRepository courseAttrRepository;

	private final String baseUri = "/course";

	@AfterEach
	void cleanupDB() {
		courseRepository.deleteAll();
		courseAttrRepository.deleteAll();
	}

	@Test
	void createCourse_ValidArg_SaveCourseToRepo() throws Exception {
		final Course courseToCreate = new Course(0, "CREATE_TEST", 1, Term.FALL, 2020);

		// Perform POST request
		MvcResult result = mockMvc
			.perform(post(this.baseUri, courseToCreate))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.code").value(courseToCreate.getCode()))
			.andExpect(jsonPath("$.level").value(courseToCreate.getLevel()))
			.andExpect(jsonPath("$.term").value(courseToCreate.getTerm().toString()))
			.andExpect(jsonPath("$.academicYear").value(courseToCreate.getAcademicYear()))
			.andReturn();
		// Extract id of created course
		int courseId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
		courseToCreate.setId(courseId);
		// Verify in repository
		assertThat(courseRepository.findAll()).hasSize(1);
		assertEquals(courseRepository.findAll().get(0), courseToCreate);
	}

	@Test
	void createCourse_MissingRequiredArgs_ValidationError() throws Exception {
		final Course courseToCreate = new Course(0, null, 6, null, null);

		// Perform POST request
		mockMvc
			.perform(post(this.baseUri, courseToCreate))
			.andExpect(status().isBadRequest()) // Expect a 400 error
			.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
			.andExpect(jsonPath("$.message").value(ExceptionMsg.METHOD_ARGUMENT_NOT_VALID))
			// Check that the validation errors are returned
			.andExpect(
				jsonPath("$.subErrors[*].field").value(containsInAnyOrder("code", "academicYear", "term", "level")));
	}

	@Test
	void createCourse_UniqueConstraintViolation_DataConflictError() throws Exception {
		// Create mock course to save to database
		Course course = new Course(0, "CONSTRAINT_CREATE_TEST", 2, Term.FALL, 2020);

		// Perform first POST request
		mockMvc.perform(post(this.baseUri, course)).andExpect(status().isCreated()); // Verify creation
		// Perform second request to violate unique constraint
		mockMvc
			.perform(post(this.baseUri, course))
			.andExpect(status().isConflict()) // Expect a 409 status
			.andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.name()))
			.andExpect(jsonPath("$.message").value(ExceptionMsg.DATABASE_CONFLICT))
			.andExpect(jsonPath("$.path").value(baseUri));
		// Verify only one course was created in database
		assertThat(courseRepository.findAll()).hasSize(1);
	}

	@Test
	void getCourses_ValidQuery_GetListOfCourses() throws Exception {
		// Create mock courses
		final Course course1 = courseRepository.save(new Course(0, "GET_TEST_1", 1, Term.FALL, 2019));
		final Course course2 = courseRepository.save(new Course(0, "GET_TEST_2", 2, Term.FALL, 2020));
		// Perform GET request
		mockMvc
			.perform(get(this.baseUri))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count").value(2))
			// Check first course
			.andExpect(jsonPath("$.data[0].id").value(course1.getId()))
			.andExpect(jsonPath("$.data[0].code").value(course1.getCode()))
			.andExpect(jsonPath("$.data[0].level").value(course1.getLevel()))
			.andExpect(jsonPath("$.data[0].term").value(course1.getTerm().toString()))
			.andExpect(jsonPath("$.data[0].academicYear").value(course1.getAcademicYear()))
			// Check second course
			.andExpect(jsonPath("$.data[1].id").value(course2.getId()))
			.andExpect(jsonPath("$.data[1].code").value(course2.getCode()))
			.andExpect(jsonPath("$.data[1].level").value(course2.getLevel()))
			.andExpect(jsonPath("$.data[1].term").value(course2.getTerm().toString()))
			.andExpect(jsonPath("$.data[1].academicYear").value(course2.getAcademicYear()));
	}

	@Test
	void deleteCourse_CourseExists_DeleteCourseInRepo() throws Exception {
		// Create mock course in repository to delete
		Course course = courseRepository.save(new Course(0, "DELETE_TEST", 1, Term.WINTER, 2000));
		final String url = String.format("%s/%d", this.baseUri, course.getId());

		// Perform DELETE request
		mockMvc.perform(delete(url)).andExpect(status().isNoContent());
		// Verify to see if course was deleted
		assertThat(courseRepository.findAll()).hasSize(0);
	}

	@Test
	void deleteCourse_CourseDoesNotExist_ElementNotFoundError() throws Exception {
		final String url = String.format("%s/%d", this.baseUri, 1);

		// Call delete endpoint, however no courses exist in the database
		mockMvc
			.perform(delete(url))
			.andExpect(status().isNotFound()) // Status should be a 404
			.andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
			.andExpect(jsonPath("$.message").value(ExceptionMsg.ELEMENT_DOES_NOT_EXIST))
			.andExpect(jsonPath("$.path").value(url));
	}

	@Test
	void createCourseAttr_ValidArg_SaveCourseAttrInRepo() throws Exception {
		// Create course to attach attribute to in repository
		Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.SUMMER, 2000));
		final String url = String.format("%s/%d/attr", this.baseUri, course.getId());
		CourseAttribute courseAttrToCreate = new CourseAttribute(0, null, CourseAttributeName.title, "TEST_VAL");

		// Perform POST request
		MvcResult result = mockMvc
			.perform(post(url, courseAttrToCreate))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value(courseAttrToCreate.getName().toString()))
			.andExpect(jsonPath("$.value").value(courseAttrToCreate.getValue()))
			.andReturn();
		// Set courseId to check
		courseAttrToCreate.setCourseId(course.getId());
		// Verify in repository
		assertThat(courseAttrRepository.findAll()).hasSize(1);
		// Check course id, name and value
		assertEquals(courseAttrRepository.findAll().get(0).getCourseId(), courseAttrToCreate.getCourseId());
		assertEquals(courseAttrRepository.findAll().get(0).getName(), courseAttrToCreate.getName());
		assertEquals(courseAttrRepository.findAll().get(0).getValue(), courseAttrToCreate.getValue());
	}

	@Test
	void createCourseAttr_CourseDoesNotExist_ElementNotFoundError() throws Exception {
		final String url = String.format("%s/%d/attr", this.baseUri, 1);

		// Create CourseAttribute to save to repo
		CourseAttribute courseAttribute = new CourseAttribute(0, null, CourseAttributeName.title, "TEST_VAL");

		// Perform post request
		mockMvc
			.perform(post(url, courseAttribute))
			.andExpect(status().isNotFound()) // Expect a 404 error
			.andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
			.andExpect(jsonPath("$.message").value(ExceptionMsg.ELEMENT_NOT_FOUND))
			.andExpect(jsonPath("$.path").value(url));
	}

	@Test
	void createCourseAttr_MissingRequiredArgs_ValidationError() throws Exception {
		// Create course to attach attribute to in repository
		Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.SUMMER, 2000));
		final String url = String.format("%s/%d/attr", this.baseUri, course.getId());

		// Create CourseAttribute missing name and value
		CourseAttribute courseAttribute = new CourseAttribute(0, null, null, null);

		// Perform POST request
		mockMvc
			.perform(post(url, courseAttribute))
			.andExpect(status().isBadRequest()) // Expect a 400 error
			.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
			.andExpect(jsonPath("$.message").value(ExceptionMsg.METHOD_ARGUMENT_NOT_VALID))
			// Check that the validation errors are returned
			.andExpect(jsonPath("$.subErrors[*].field").value(containsInAnyOrder("name", "value")));
	}

	@Test
	void createCourseAttr_AttributeNameAlreadyExists_OverwriteAttributeValue() throws Exception {
		// Create course to attach attribute to in repository
		Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.SUMMER, 2000));
		final String url = String.format("%s/%d/attr", this.baseUri, course.getId());

		// Create CourseAttribute to save (twice) to repo
		CourseAttribute courseAttribute = new CourseAttribute(0, null, CourseAttributeName.title, "TEST_VAL");

		// Perform first POST request
		mockMvc
			.perform(post(url, courseAttribute))
			.andExpect(status().isCreated()) // Verify 201 on first request
			.andExpect(jsonPath("$.value").value(courseAttribute.getValue()));

		// Set new value
		courseAttribute.setValue("NEW_TEST_VAL");
		// Perform second POST request to overwrite the first value
		mockMvc
			.perform(post(url, courseAttribute))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value(courseAttribute.getName().toString()))
			.andExpect(jsonPath("$.value").value(courseAttribute.getValue()))
			.andReturn();
		// Verify only one course attribute is in database
		assertThat(courseAttrRepository.findAll()).hasSize(1);
		// Check that value is overwritten
		assertEquals(courseAttrRepository.findAll().get(0).getValue(), courseAttribute.getValue());
	}

	@Test
	void getCourseAttrs_ValidQuery_ReturnListOfCourseAttributes() throws Exception {
		Course course = courseRepository.save(new Course(0, "TEST_GET_COURSE_ATTR", 1, Term.WINTER, 2020));
		String url = String.format("%s/%d/attr", this.baseUri, course.getId());
		// Create mock entities
		CourseAttribute attr1 =
			courseAttrRepository.save(new CourseAttribute(0, course.getId(), CourseAttributeName.title, "TEST_VAL_1"));
		CourseAttribute attr2 = courseAttrRepository
			.save(new CourseAttribute(0, course.getId(), CourseAttributeName.professorName, "TEST_VAL_2"));

		// Perform GET request
		mockMvc
			.perform(get(url))
			.andExpect(status().isOk())
			// Check title
			.andExpect(jsonPath("$.title").value(attr1.getValue()))
			// Check professorName
			.andExpect(jsonPath("$.professorName").value(attr2.getValue()));
	}

	@Test
	void deleteCourseAttr_CourseAttributeExists_DeleteCourseAttributeInRepo() throws Exception {
		// Create mock courseAttribute in repository to delete
		Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.WINTER, 2000));
		CourseAttribute courseAttribute =
			courseAttrRepository.save(new CourseAttribute(0, course.getId(), CourseAttributeName.title, "TEST_VALUE"));
		final String url =
			String.format("%s/%d/attr/%s", this.baseUri, course.getId(), courseAttribute.getName().toString());

		// Perform DELETE request
		mockMvc.perform(delete(url)).andExpect(status().isNoContent());
		// Check to see if course attribute was deleted
		assertThat(courseAttrRepository.findAll()).hasSize(0);
	}

	@Test
	void deleteCourseAttr_CourseAttributeNameDoesNotExist_ElementNotFoundError() throws Exception {
		final String url = String.format("%s/%d/attr/%s", this.baseUri, 1, "INVALID_ATTR_NAME");

		// Call delete endpoint, no CourseAttributeName with the name INVALID_ATTR_NAME exists
		mockMvc
			.perform(delete(url))
			.andExpect(status().isBadRequest()) // Status should be a 400
			.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
			.andExpect(jsonPath("$.message").value(ExceptionMsg.MALFORMED_JSON_REQUEST))
			.andExpect(jsonPath("$.path").value(url));
	}
}
