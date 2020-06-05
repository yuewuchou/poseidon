package com.xw.poseidon.utils.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;

import com.xw.poseidon.utils.PoseidonStringUtils;

public class PoseidonJavaGenerator extends AbstractJavaGenerator {
	
	public PoseidonJavaGenerator() {
		super();
	}

	@Override
	public List<CompilationUnit> getCompilationUnits() {
		String daoPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
		String servicePackage = daoPackage.replace("dao", "service");
		String controllerPackage = daoPackage.replace("dao", "controller");
		String entityPackage = daoPackage.replace("dao", "entity");
		
		StringBuilder sb = new StringBuilder();
		sb.append(daoPackage)
		  .append(".")
		  .append(introspectedTable.getFullyQualifiedTable().getDomainObjectName())
		  .append("Mapper");
		FullyQualifiedJavaType daoType = new FullyQualifiedJavaType(sb.toString());
		
		sb.delete(0, sb.length())
		  .append(servicePackage)
		  .append(".")
		  .append(introspectedTable.getFullyQualifiedTable().getDomainObjectName())
		  .append("Service");
		FullyQualifiedJavaType serviceType = new FullyQualifiedJavaType(sb.toString());
		
		sb.delete(0, sb.length())
		  .append(servicePackage)
		  .append(".impl.")
		  .append(introspectedTable.getFullyQualifiedTable().getDomainObjectName())
		  .append("ServiceImpl");
		FullyQualifiedJavaType serviceImplType = new FullyQualifiedJavaType(sb.toString());
		
		sb.delete(0, sb.length())
		  .append(controllerPackage)
		  .append(".")
		  .append(introspectedTable.getFullyQualifiedTable().getDomainObjectName())
		  .append("Controller");
		FullyQualifiedJavaType controllerType = new FullyQualifiedJavaType(sb.toString());
		
		Interface service = buildService(serviceType,entityPackage);
		TopLevelClass serviceImpl = buildServiceImpl(daoType,serviceType,serviceImplType,entityPackage);
		TopLevelClass controller = buildController(serviceType,controllerType,entityPackage);
		
		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		answer.add(service);
		answer.add(serviceImpl);
		answer.add(controller);
		return answer;
	}
	
	protected Interface buildService(FullyQualifiedJavaType serviceType,String entityPackage) {
        Interface answer = new Interface(serviceType);
        answer.setVisibility(JavaVisibility.PUBLIC);
        
        answer.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        answer.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".PagedParameterEntity"));
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".PagedResultEntity"));

        context.getCommentGenerator().addJavaFileComment(answer);
        
        addServiceMethodAdd(answer);
        addServiceMethodAddBatch(answer);
        addServiceMethodRemoveById(answer);
        addServiceMethodModifyById(answer);
        addServiceMethodQueryByCondition(answer);
        addServiceMethodQueryPageByCondition(answer);

        return answer;
    }
	
	private void addServiceMethodAdd(Interface answer) {
		Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("add");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        answer.addMethod(method);
	}
	
	private void addServiceMethodAddBatch(Interface answer) {
		Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("addBatch");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<"+introspectedTable.getBaseRecordType()+">"), "list"));
        answer.addMethod(method);
	}
	
	private void addServiceMethodRemoveById(Interface answer) {
		Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("removeById");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"), "id"));
        answer.addMethod(method);
	}
	
	private void addServiceMethodModifyById(Interface answer) {
		Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("modifyById");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        answer.addMethod(method);
	}
	
	private void addServiceMethodQueryByCondition(Interface answer) {
		Method method = new Method();
        method.setReturnType(new FullyQualifiedJavaType("List<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("queryByCondition");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        answer.addMethod(method);
	}
	
	private void addServiceMethodQueryPageByCondition(Interface answer) {
		Method method = new Method();
        method.setReturnType(new FullyQualifiedJavaType("PagedResultEntity<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("queryPageByCondition");
        method.addParameter(0,new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        method.addParameter(1,new Parameter(new FullyQualifiedJavaType("PagedParameterEntity"), "pagedParameter"));
        answer.addMethod(method);
	}
	
	protected TopLevelClass buildServiceImpl(FullyQualifiedJavaType daoType,FullyQualifiedJavaType serviceType,FullyQualifiedJavaType serviceImplType,String entityPackage) {
        TopLevelClass answer = new TopLevelClass(serviceImplType);
        answer.setVisibility(JavaVisibility.PUBLIC);
        answer.addSuperInterface(serviceType);
        
        answer.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional"));
        answer.addImportedType(daoType);
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".PagedParameterEntity"));
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".PagedResultEntity"));
        answer.addImportedType(serviceType);
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        answer.addImportedType(modelType);
        
        answer.addAnnotation("@Service");
        
        Field daoField = new Field(PoseidonStringUtils.uncapitalize(daoType.getShortName()),daoType);
        daoField.setVisibility(JavaVisibility.PRIVATE);
        daoField.addAnnotation("@Autowired");
        answer.addField(daoField);
        
        Field logField = new Field();
        FullyQualifiedJavaType logType = new FullyQualifiedJavaType("org.slf4j.Logger");
        FullyQualifiedJavaType logFactoryType = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
        StringBuilder sb = new StringBuilder();
        logField.setVisibility(JavaVisibility.PRIVATE);
        logField.setStatic(true);
        logField.setFinal(true);
        logField.setType(logType);
        logField.setName("logger");
        logField.setInitializationString(
                sb.delete(0, sb.length())
                        .append("LoggerFactory.getLogger(")
                        .append(serviceImplType.getShortName())
                        .append(".class)")
                        .toString()
        );
        answer.addField(logField);
        answer.addImportedType(logType);
        answer.addImportedType(logFactoryType);

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(answer);
        
        this.addServiceImplMethodAdd(answer, daoType, sb);
        this.addServiceImplMethodAddBatch(answer, daoType, sb);
        this.addServiceImplMethodRemoveById(answer, daoType, sb);
        this.addServiceImplMethodModifyById(answer, daoType, sb);
        this.addServiceImplMethodQueryByCondition(answer, daoType, modelType, sb);
        this.addServiceImplMethodQueryPageByCondition(answer, daoType, modelType, sb);
        
        return answer;
    }
	
	private void addServiceImplMethodAdd(TopLevelClass answer,FullyQualifiedJavaType daoType,StringBuilder sb) {
		Method method = new Method();
        method.addAnnotation("@Override");
        method.addAnnotation("@Transactional(rollbackFor = Exception.class)");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName("add");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
		
        sb.delete(0, sb.length())
	  	  .append("int count = ")
	  	  .append(PoseidonStringUtils.uncapitalize(daoType.getShortName()))
	  	  .append(".insertSelective(record);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("return count;");
	  	method.addBodyLine(sb.toString());
	  	
        answer.addMethod(method);
	}
	
	private void addServiceImplMethodAddBatch(TopLevelClass answer,FullyQualifiedJavaType daoType,StringBuilder sb) {
		Method method = new Method();
        
        method.addAnnotation("@Override");
        method.addAnnotation("@Transactional(rollbackFor = Exception.class)");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName("addBatch");
		method.addParameter(new Parameter(new FullyQualifiedJavaType("List<"+introspectedTable.getBaseRecordType()+">"), "list"));
		
		sb.delete(0, sb.length())
	  	  .append("int count = ")
	  	  .append(PoseidonStringUtils.uncapitalize(daoType.getShortName()))
	  	  .append(".insertBatch(list);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("return count;");
	  	method.addBodyLine(sb.toString());
  	
        answer.addMethod(method);
	}
	
	private void addServiceImplMethodRemoveById(TopLevelClass answer,FullyQualifiedJavaType daoType,StringBuilder sb) {
		Method method = new Method();
        
        method.addAnnotation("@Override");
        method.addAnnotation("@Transactional(rollbackFor = Exception.class)");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName("removeById");
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"), "id"));
		
		sb.delete(0, sb.length())
	  	  .append("int count = ")
	  	  .append(PoseidonStringUtils.uncapitalize(daoType.getShortName()))
	  	  .append(".deleteByPrimaryKey(id);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("return count;");
	  	method.addBodyLine(sb.toString());	
        
        answer.addMethod(method);
	}
	
	private void addServiceImplMethodModifyById(TopLevelClass answer,FullyQualifiedJavaType daoType,StringBuilder sb) {
		Method method = new Method();
        
        method.addAnnotation("@Override");
        method.addAnnotation("@Transactional(rollbackFor = Exception.class)");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName("modifyById");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
		
		sb.delete(0, sb.length())
	  	  .append("int count = ")
	  	  .append(PoseidonStringUtils.uncapitalize(daoType.getShortName()))
	  	  .append(".updateByPrimaryKeySelective(record);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("return count;");
	  	method.addBodyLine(sb.toString());
		
        answer.addMethod(method);
	}
	
	private void addServiceImplMethodQueryByCondition(TopLevelClass answer,FullyQualifiedJavaType daoType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        
        method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("List<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">"));
		method.setName("queryByCondition");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
		
		sb.delete(0, sb.length())
	  	  .append("List<")
	  	  .append(modelType.getShortName())
	  	  .append("> list = ")
	  	  .append(PoseidonStringUtils.uncapitalize(daoType.getShortName()))
	  	  .append(".selectByCondition(record);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("return list;");
	  	method.addBodyLine(sb.toString());
		
        answer.addMethod(method);
	}
	
	private void addServiceImplMethodQueryPageByCondition(TopLevelClass answer,FullyQualifiedJavaType daoType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        
        method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("PagedResultEntity<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">"));
		method.setName("queryPageByCondition");
        method.addParameter(0,new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        method.addParameter(1,new Parameter(new FullyQualifiedJavaType("PagedParameterEntity"), "pagedParameter"));
        
        sb.delete(0, sb.length())
	  	  .append("Integer pageNo = pagedParameter.getPageNo();");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("Integer pageSize = pagedParameter.getPageSize();");
	  	method.addBodyLine(sb.toString());  
	  	sb.delete(0, sb.length())
	  	  .append("List<")
	  	  .append(modelType.getShortName())
	  	  .append("> list = ")
	  	  .append(PoseidonStringUtils.uncapitalize(daoType.getShortName()))
	  	  .append(".selectPageByCondition(record, (pageNo - 1) * pageSize, pageSize);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("Integer rowCount = ")
	  	  .append(PoseidonStringUtils.uncapitalize(daoType.getShortName()))
	  	  .append(".selectPageByConditionCount(record);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("logger.info(\"queryPageByCondition-list:{}\",list);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("logger.info(\"queryPageByCondition-rowCount:{}\",rowCount);");
	  	method.addBodyLine(sb.toString());
	  	sb.delete(0, sb.length())
	  	  .append("return new PagedResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(pageNo, pageSize, rowCount, list);");
	  	method.addBodyLine(sb.toString());
  	
        answer.addMethod(method);
	}
	
	protected TopLevelClass buildController(FullyQualifiedJavaType serviceType,FullyQualifiedJavaType controllerType,String entityPackage) {
        TopLevelClass answer = new TopLevelClass(controllerType);
        answer.setVisibility(JavaVisibility.PUBLIC);
        
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Controller"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PostMapping"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ResponseBody"));
        answer.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestBody"));
        
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".ResultEntity"));
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".ResultEnum"));
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".PagedResultEntity"));
        answer.addImportedType(new FullyQualifiedJavaType(entityPackage + ".PagedParameterEntity"));
        answer.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        answer.addImportedType(serviceType);
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        answer.addImportedType(modelType);
        
        answer.addAnnotation("@Controller");
        
        StringBuilder sb = new StringBuilder();
        sb.append("@RequestMapping(\"/")
          .append(PoseidonStringUtils.uncapitalize(modelType.getShortName()))
          .append("\")");
        answer.addAnnotation(sb.toString());
        
        Field logField = new Field();
        FullyQualifiedJavaType logType = new FullyQualifiedJavaType("org.slf4j.Logger");
        FullyQualifiedJavaType logFactoryType = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
        logField.setVisibility(JavaVisibility.PRIVATE);
        logField.setStatic(true);
        logField.setFinal(true);
        logField.setType(logType);
        logField.setName("logger");
        logField.setInitializationString(
                sb.delete(0, sb.length())
                        .append("LoggerFactory.getLogger(")
                        .append(controllerType.getShortName())
                        .append(".class)")
                        .toString()
        );
        answer.addField(logField);
        answer.addImportedType(logType);
        answer.addImportedType(logFactoryType);
        
        Field serviceField = new Field(PoseidonStringUtils.uncapitalize(serviceType.getShortName()),serviceType);
        serviceField.setVisibility(JavaVisibility.PRIVATE);
        serviceField.addAnnotation("@Autowired");
        answer.addField(serviceField);

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(answer);
        
        this.addControllerMethodAdd(answer, serviceType, modelType, sb);
        this.addControllerMethodAddBatch(answer, serviceType, modelType, sb);
        this.addControllerMethodRemoveById(answer, serviceType, modelType, sb);
        this.addControllerMethodModifyById(answer, serviceType, modelType, sb);
        this.addControllerMethodQueryByCondition(answer, serviceType, modelType, sb);
        this.addControllerMethodQueryPageByCondition(answer, serviceType, modelType, sb);
        
        return answer;
    }
	
	private void addControllerMethodAdd(TopLevelClass answer,FullyQualifiedJavaType serviceType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        method.addAnnotation("@PostMapping(\"/add\")");
        method.addAnnotation("@ResponseBody");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResultEntity<"+introspectedTable.getBaseRecordType()+">"));
		method.setName("add");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
		
		sb.delete(0, sb.length())
		  .append("try {");
		method.addBodyLine(sb.toString());
		
        sb.delete(0, sb.length())
	  	  .append(PoseidonStringUtils.uncapitalize(serviceType.getShortName()))
	  	  .append(".add(record);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), record);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("} catch (Exception e) {");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
		  .append("logger.error(\"新增用户失败\", e);");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("}");
		method.addBodyLine(sb.toString());
	  	
        answer.addMethod(method);
	}
	
	private void addControllerMethodAddBatch(TopLevelClass answer,FullyQualifiedJavaType serviceType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        method.addAnnotation("@PostMapping(\"/addBatch\")");
        method.addAnnotation("@ResponseBody");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResultEntity<"+introspectedTable.getBaseRecordType()+">"));
		method.setName("addBatch");
		method.addParameter(new Parameter(new FullyQualifiedJavaType("List<"+introspectedTable.getBaseRecordType()+">"), "list","@RequestBody"));
		
		sb.delete(0, sb.length())
		  .append("try {");
		method.addBodyLine(sb.toString());
		
        sb.delete(0, sb.length())
	  	  .append(PoseidonStringUtils.uncapitalize(serviceType.getShortName()))
	  	  .append(".addBatch(list);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("} catch (Exception e) {");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
		  .append("logger.error(\"批量新增用户失败\", e);");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("}");
		method.addBodyLine(sb.toString());
	  	
        answer.addMethod(method);
	}
	
	private void addControllerMethodRemoveById(TopLevelClass answer,FullyQualifiedJavaType serviceType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        method.addAnnotation("@PostMapping(\"/removeById\")");
        method.addAnnotation("@ResponseBody");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResultEntity<"+introspectedTable.getBaseRecordType()+">"));
		method.setName("addBatch");
		method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "id"));
		
		sb.delete(0, sb.length())
		  .append("try {");
		method.addBodyLine(sb.toString());
		
        sb.delete(0, sb.length())
	  	  .append(PoseidonStringUtils.uncapitalize(serviceType.getShortName()))
	  	  .append(".removeById(id);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("} catch (Exception e) {");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
		  .append("logger.error(\"删除用户失败\", e);");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("}");
		method.addBodyLine(sb.toString());
	  	
        answer.addMethod(method);
	}
	
	private void addControllerMethodModifyById(TopLevelClass answer,FullyQualifiedJavaType serviceType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        method.addAnnotation("@PostMapping(\"/modifyById\")");
        method.addAnnotation("@ResponseBody");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResultEntity<"+introspectedTable.getBaseRecordType()+">"));
		method.setName("modifyById");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
		
		sb.delete(0, sb.length())
		  .append("try {");
		method.addBodyLine(sb.toString());
		
        sb.delete(0, sb.length())
	  	  .append(PoseidonStringUtils.uncapitalize(serviceType.getShortName()))
	  	  .append(".modifyById(record);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("} catch (Exception e) {");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
		  .append("logger.error(\"修改用户失败\", e);");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("}");
		method.addBodyLine(sb.toString());
	  	
        answer.addMethod(method);
	}
	
	private void addControllerMethodQueryByCondition(TopLevelClass answer,FullyQualifiedJavaType serviceType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        method.addAnnotation("@PostMapping(\"/queryByCondition\")");
        method.addAnnotation("@ResponseBody");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResultEntity<List<"+introspectedTable.getBaseRecordType()+">>"));
		method.setName("queryByCondition");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
		
		sb.delete(0, sb.length())
		  .append("try {");
		method.addBodyLine(sb.toString());
		
        sb.delete(0, sb.length())
          .append("List<")
          .append(modelType.getShortName())
          .append("> list = ")
	  	  .append(PoseidonStringUtils.uncapitalize(serviceType.getShortName()))
	  	  .append(".queryByCondition(record);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<List<")
	  	  .append(modelType.getShortName())
	  	  .append(">>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), list);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("} catch (Exception e) {");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
		  .append("logger.error(\"查询用户失败\", e);");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<List<")
	  	  .append(modelType.getShortName())
	  	  .append(">>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("}");
		method.addBodyLine(sb.toString());
	  	
        answer.addMethod(method);
	}
	
	private void addControllerMethodQueryPageByCondition(TopLevelClass answer,FullyQualifiedJavaType serviceType,FullyQualifiedJavaType modelType,StringBuilder sb) {
		Method method = new Method();
        method.addAnnotation("@PostMapping(\"/queryPageByCondition\")");
        method.addAnnotation("@ResponseBody");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResultEntity<PagedResultEntity<"+introspectedTable.getBaseRecordType()+">>"));
		method.setName("queryPageByCondition");
		method.addParameter(0,new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
		method.addParameter(1,new Parameter(new FullyQualifiedJavaType("PagedParameterEntity"), "pagedParameter"));
		
		sb.delete(0, sb.length())
		  .append("try {");
		method.addBodyLine(sb.toString());
		
        sb.delete(0, sb.length())
          .append("PagedResultEntity<")
          .append(modelType.getShortName())
          .append("> pagedList = ")
	  	  .append(PoseidonStringUtils.uncapitalize(serviceType.getShortName()))
	  	  .append(".queryPageByCondition(record, pagedParameter);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<PagedResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), pagedList);");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("} catch (Exception e) {");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
		  .append("logger.error(\"分页查询用户失败\", e);");
		method.addBodyLine(sb.toString());
		
		sb.delete(0, sb.length())
	  	  .append("return new ResultEntity<PagedResultEntity<")
	  	  .append(modelType.getShortName())
	  	  .append(">>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());");
	  	method.addBodyLine(sb.toString());
	  	
	  	sb.delete(0, sb.length())
		  .append("}");
		method.addBodyLine(sb.toString());
	  	
        answer.addMethod(method);
	}
}
