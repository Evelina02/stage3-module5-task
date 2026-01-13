package com.mjc.school.controller.impl;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.assertj.core.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;


@ArchTag("architecture")
@AnalyzeClasses(packagesOf = ControllerLayerArchitectureTest.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class ControllerLayerArchitectureTest {

    @ArchTest
    void classesAnnotatedWithRestControllerShouldResideInControllerPackage(final JavaClasses controllerLayerClasses) {
        classes()
                .that()
                .areAnnotatedWith(RestController.class)
                .should()
                .resideInAPackage("..controller..")
                .because("it's general package for rest controllers annotated with @RestController")
                .check(controllerLayerClasses);
    }

    @ArchTest
    void classesAnnotatedWithRestControllerShouldEndWithController(final JavaClasses controllerLayerClasses) {
        classes()
                .that()
                .areAnnotatedWith(RestController.class)
                .should()
                .haveSimpleNameEndingWith("Controller")
                .because("Classes annotated with @RestController should meet name convention -  class name ends with Controller")
                .check(controllerLayerClasses);
    }

    @ArchTest
    void classesAnnotatedWithRestControllerShouldBeAnnotatedWithRequestMapping(final JavaClasses controllerLayerClasses) {
        classes()
                .that()
                .areAnnotatedWith(RestController.class)
                .should()
                .beAnnotatedWith(RequestMapping.class)
                .because("Classes annotated with @RestController should be annotated with @RequestMapping(value = (rest api root path), produces = (rest api media type)")
                .check(controllerLayerClasses);
    }

    @ArchTest
    void classesAnnotatedWithRestControllerShouldNotAccessPersistenceLayer(final JavaClasses controllerLayerClasses) {
        noClasses()
                .should()
                .accessClassesThat()
                .areAnnotatedWith(Repository.class)
                .orShould()
                .resideInAPackage("..repository..")
                .because("Controller layer should not access persistence layer.")
                .check(controllerLayerClasses);
    }

    @ArchTest
    void classesAnnotatedWithRestControllerShouldNotDependOnPersistenceLayer(final JavaClasses controllerLayerClasses) {
        noClasses()
                .should()
                .dependOnClassesThat()
                .areAnnotatedWith(Repository.class)
                .orShould()
                .resideInAPackage("..repository..")
                .because("Controller layer should not depend on persistence layer.")
                .check(controllerLayerClasses);
    }


    @ArchTest
    void publicMethodsDeclaredInRestControllersShouldNotBeAnnotatedWithResponseBody(final JavaClasses controllerLayerClasses) {
        methods().that().areDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class)
                .and()
                .arePublic()
                .should()
                .notBeAnnotatedWith(ResponseBody.class)
                .because("Public methods declared in classes annotated with @RestController should not be annotated with @ResponseBody")
                .check(controllerLayerClasses);
    }

    @ArchTest
    void getMethodsDeclaredInRestControllersShouldMeetRequirementsForGetRestAPI(final JavaClasses controllerLayerClasses) {
        methods().that().areDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class)
                .and()
                .arePublic().and().haveNameStartingWith("get")
                .or().haveNameStartingWith("find")
                .or().haveNameStartingWith("retrieve")
                .or().haveNameStartingWith("read")
                .should()
                .beAnnotatedWith(GetMapping.class)
                .andShould(haveParameters())
                .andShould().notHaveRawReturnType(EntityResponse.class)
                .andShould(returnDTO())
                .orShould().haveRawReturnType(Collection.class)
                .because("Get methods declared in classes annotated with @RestController should meet requirements for GET REST API")
                .check(controllerLayerClasses);
    }

    @ArchTest
    void postMethodsDeclaredInRestControllersShouldMeetRequirementsForPostRestAPI(final JavaClasses controllerLayerClasses) {
        methods().that().areDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class)
                .and()
                .arePublic().and().haveNameStartingWith("create")
                .or().haveNameStartingWith("save")
                .or().haveNameStartingWith("add")
                .or().haveNameStartingWith("insert")
                .should()
                .beAnnotatedWith(PostMapping.class)
                .andShould(haveAnnotatedWithValue(ResponseStatus.class, HttpStatus.CREATED.toString()))
                .andShould(takeDtoAsParameter())
                .andShould(haveAllParametersAnnotatedWith(RequestBody.class))
                .andShould().notHaveRawReturnType(EntityResponse.class)
                .andShould(returnDTO())
                .because("Post methods declared in classes annotated with @RestController should meet requirements for POST REST API")
                .check(controllerLayerClasses);
    }


    @ArchTest
    void putMethodsDeclaredInRestControllersShouldMeetRequirementsForPutOrPatchRestAPU(final JavaClasses controllerLayerClasses) {
        methods().that().areDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class)
                .and()
                .arePublic().and().haveNameStartingWith("update")
                .or().haveNameStartingWith("modify")
                .or().haveNameStartingWith("change")
                .or().haveNameStartingWith("patch")
                .should()
                .beAnnotatedWith(PutMapping.class)
                .orShould()
                .beAnnotatedWith(PatchMapping.class)
                .andShould(takeDtoAsParameter())
                .andShould(haveParameterAnnotatedWith(PathVariable.class))
                .andShould(haveParameterAnnotatedWith(RequestBody.class))
                .andShould().notHaveRawReturnType(EntityResponse.class)
                .andShould(returnDTO())
                .because("Put or Patch methods declared in classes annotated with @RestController should meet requirements for PUT OR PATCH REST API")
                .check(controllerLayerClasses);
    }

    @ArchTest
    void deleteMethodsDeclaredInRestControllersShouldMeetRequirementsForDeleteRestAPI(final JavaClasses controllerLayerClasses) {
        methods().that().areDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class)
                .and()
                .arePublic().and().haveNameStartingWith("delete")
                .or().haveNameStartingWith("remove")
                .should()
                .beAnnotatedWith(DeleteMapping.class)
                .andShould(haveAnnotatedWithValue(ResponseStatus.class, HttpStatus.NO_CONTENT.toString()))
                .andShould(haveParameterAnnotatedWith(PathVariable.class))
                .andShould().notHaveRawReturnType(EntityResponse.class)
                .andShould().haveRawReturnType(Void.TYPE)
                .because("Delete methods declared in classes annotated with @RestController should meet requirements for DELETE REST API")
                .check(controllerLayerClasses);
    }

    private ArchCondition<JavaMethod> haveAnnotatedWithValue(final Class<? extends Annotation> annotationClass, final String annotationValue) {
        return new ArchCondition<JavaMethod>("have all parameters annotated with @" + annotationClass.getSimpleName()) {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                boolean isMethodAnnotated = false;
                for (Annotation methodAnnotation : method.reflect().getAnnotations()) {
                    if (methodAnnotation.annotationType().equals(annotationClass) && methodAnnotation.toString().contains(annotationValue)) {
                        isMethodAnnotated = true;
                    }
                }
                String message = (isMethodAnnotated ? "" : "not ")
                        + method.getDescription()
                        + " is annotated with @" + annotationClass.getSimpleName() + " with the value = " + annotationValue;
                events.add(new SimpleConditionEvent(method, isMethodAnnotated, message));
            }
        };
    }

    private ArchCondition<JavaMethod> haveParameterAnnotatedWith(Class<? extends Annotation> annotationClass) {
        return new ArchCondition<JavaMethod>("have a parameter annotated with @" + annotationClass.getSimpleName()) {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                boolean isParameterAnnotated = false;
                for (Annotation[] parameterAnnotations : method.reflect().getParameterAnnotations()) {
                    for (Annotation annotation : parameterAnnotations) {
                        if (annotation.annotationType().equals(annotationClass)) {
                            isParameterAnnotated = true;
                        }
                    }
                }
                String message = (isParameterAnnotated ? "" : "not ")
                        + "at least one parameters of " + method.getDescription()
                        + " is annotated with @" + annotationClass.getSimpleName();
                events.add(new SimpleConditionEvent(method, isParameterAnnotated, message));
            }
        };
    }

    private ArchCondition<JavaMethod> haveAllParametersAnnotatedWith(Class<? extends Annotation> annotationClass) {
        return new ArchCondition<JavaMethod>("have all parameters annotated with @" + annotationClass.getSimpleName()) {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                boolean areAllParametersAnnotated = true;
                for (Annotation[] parameterAnnotations : method.reflect().getParameterAnnotations()) {
                    boolean isParameterAnnotated = false;
                    for (Annotation annotation : parameterAnnotations) {
                        if (annotation.annotationType().equals(annotationClass)) {
                            isParameterAnnotated = true;
                        }
                    }
                    areAllParametersAnnotated &= isParameterAnnotated;
                }
                String message = (areAllParametersAnnotated ? "" : "not ")
                        + "all parameters of " + method.getDescription()
                        + " are annotated with @" + annotationClass.getSimpleName();
                events.add(new SimpleConditionEvent(method, areAllParametersAnnotated, message));
            }
        };
    }

    private ArchCondition<JavaMethod> returnDTO() {
        return new ArchCondition<JavaMethod>("rest method returns DTO") {
            @Override
            public void check(final JavaMethod method, final ConditionEvents events) {
                final Type returnType = method.reflect().getReturnType();
                if(returnType.getTypeName().toUpperCase().contains("DTO")) {
                    final String message = method.getDescription()
                            + " returns DTO object: " + returnType.getTypeName();
                    events.add(new SimpleConditionEvent(method, true, message));
                }
            }
        };
    }

    private ArchCondition<JavaMethod> takeDtoAsParameter() {
        return new ArchCondition<JavaMethod>("rest method takes DTO") {
            @Override
            public void check(final JavaMethod method, final ConditionEvents events) {
                final Type[] parameterTypes = method.reflect().getParameterTypes();
                for (Type parameterType : parameterTypes) {
                    if (parameterType.getTypeName().toUpperCase().contains("DTO")) {
                        final String message = method.getDescription()
                                + " takes DTO object: " + parameterType.getTypeName() + " as parameter";
                        events.add(new SimpleConditionEvent(method, true, message));
                    }
                }
            }
        };
    }

    private ArchCondition<JavaMethod> haveParameters() {
        return new ArchCondition<JavaMethod>("rest method has parameters") {
            @Override
            public void check(final JavaMethod method, final ConditionEvents events) {
                final Type[] parameterTypes = method.reflect().getParameterTypes();
                if (Arrays.isNullOrEmpty(parameterTypes)) {
                    final String message = method.getDescription()
                            + " does not have any parameters ";
                    events.add(new SimpleConditionEvent(method, false, message));
                    return;
                }
                final String message = method.getDescription()
                        + " have " + parameterTypes.length + " parameters";
                events.add(new SimpleConditionEvent(method, true, message));
            }
        };
    }
}
