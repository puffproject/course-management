package com.unityTest.courseManagement.restApi;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.models.api.response.CourseAttributeView;
import com.unityTest.courseManagement.models.api.response.page.CoursePage;
import io.swagger.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Course Management API", tags = "Course API", description = "Manage course resources")
@Validated
@RequestMapping(value = "/course")
public interface CourseApi extends BaseApi {
    /**
     * POST endpoint to create a Course
     * @return Created course
     */
    @ApiOperation(value = "Create or update a course", nickname = "createCourse", response = Course.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({ @ApiResponse(code = 201, message = "Created", response = Course.class) })
    @PostMapping(value = "",  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Course> createCourse(@ApiParam(value = "Course to create", required = true) @Valid @RequestBody Course course);

    /**
     * GET endpoint to retrieve courses
     * Filter by id, code, level, term and academic year
     * @return List of Course objects matching criterion
     */
    @ApiOperation(value = "Retrieve a list of courses", nickname = "getCourses", response = CoursePage.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CoursePage> getCourses(
            Pageable pageable,
            @ApiParam("Course id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("Course code") @RequestParam(value = "code", required = false) String code,
            @ApiParam("Course level") @RequestParam(value = "level", required = false) Integer level,
            @ApiParam("Course term") @RequestParam(value = "term", required = false) String term,
            @ApiParam("Course academic year") @RequestParam(value = "academicYear", required = false) Integer academicYear
    );

    /**
     * DELETE endpoint to delete a course
     * @param courseId Id of course to delete
     */
    @ApiOperation(value = "Delete a course", nickname = "deleteCourse")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{courseId}")
    void deleteCourse(@ApiParam(value = "Course id", required = true) @PathVariable(value = "courseId") Integer courseId);

    /**
     * POST endpoint to create an attribute for a course
     * @return Create course attribute
     */
    @ApiOperation(value = "Create or update a course attribute", nickname = "createCourseAttr", response = CourseAttribute.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({ @ApiResponse(code = 201, message = "Created", response = CourseAttribute.class) })
    @PostMapping(value = "/{courseId}/attr", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CourseAttribute> createCourseAttr(
            @ApiParam(value = "Course id", required = true) @PathVariable(value = "courseId") Integer courseId,
            @ApiParam(value = "Course attribute to create", required = true) @Valid @RequestBody CourseAttribute courseAttr
    );

    /**
     * GET endpoint to retrieve attributes for a course
     * @return Collection of attributes for a course
     */
    @ApiOperation(value = "Get attributes for a course", nickname = "getCourseAttrs", response = CourseAttributeView.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{courseId}/attr", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CourseAttributeView> getCourseAttrs(@ApiParam(value = "Course id", required = true) @PathVariable(value = "courseId") Integer courseId);

    /**
     * DELETE endpoint to delete a course attribute
     * @param courseId Id of course attribute belongs to
     * @param attributeName Name of attribute to delete
     */
    @ApiOperation(value = "Delete a course attribute", nickname = "deleteCourseAttr")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{courseId}/attr/{attributeName}")
    void deleteCourseAttr(
            @ApiParam(value = "Course id", required = true) @PathVariable(value = "courseId") Integer courseId,
            @ApiParam(value = "Course attribute name", required = true) @PathVariable(value = "attributeName") String attributeName);
}
