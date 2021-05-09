package net.mkaminski.sri4soap;

import lombok.RequiredArgsConstructor;
import net.mkaminski.sri4_soap.employees.*;
import net.mkaminski.sri4soap.model.Employee;
import net.mkaminski.sri4soap.repo.EmployeeRepository;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Endpoint
@RequiredArgsConstructor
public class EmployeeEndpoint {
    private final EmployeeRepository employeeRepository;

    @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "getEmployeesRequest")
    @ResponsePayload
    public GetEmployeesResponce getEmployees(@RequestPayload GetEmployeesRequest request){
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeDto> dtoList = employeeList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        GetEmployeesResponce res = new GetEmployeesResponce();
        res.getEmployees().addAll(dtoList);
        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE,localPart = "getEmployeeByIdRequest")
    @ResponsePayload
    public GetEmployeeByIdResponse getEmployeeById(@RequestPayload GetEmployeeByIdRequest request){
        Long employeeId = request.getEmployeeId().longValue();
        Optional<Employee> emp = employeeRepository.findById(employeeId);
        GetEmployeeByIdResponse res = new GetEmployeeByIdResponse();
        res.setEmployee(convertToDto(emp.orElse(null)));
        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig.EMPLOYEE_NAMESPACE, localPart = "addEmployeeRequest")
    @ResponsePayload
    public AddEmployeeResponse addEmployee(@RequestPayload AddEmployeeRequest reqest){
        EmployeeDto empDto =reqest.getEmployee();
        Employee emp = convertToEntity(empDto);
        employeeRepository.save(emp);
        AddEmployeeResponse response = new AddEmployeeResponse();
        response.setEmployeeId(new BigDecimal(emp.getId()));
        return response;
    }
    private EmployeeDto convertToDto(Employee e){
        if (e==null){
            return null;
        }
        try{
            EmployeeDto dto = new EmployeeDto();
            dto.setId(new BigDecimal(e.getId()));
            dto.setFirstName(e.getFirstName());
            dto.setLastName(e.getLastName());
            XMLGregorianCalendar birthDate = null;
            birthDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(e.getBirthDate().toString());
            dto.setBirthDate(birthDate);
            dto.setJob(e.getJob());

            return dto;
        } catch(DatatypeConfigurationException datatypeConfigurationException){
            datatypeConfigurationException.printStackTrace();
            return null;
        }
    }

    private Employee convertToEntity(EmployeeDto dto){
        return Employee.builder()
                .id(dto.getId() !=null ? dto.getId().longValue() : null)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .job(dto.getJob())
                .build();
    }
}
