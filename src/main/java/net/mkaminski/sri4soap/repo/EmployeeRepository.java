package net.mkaminski.sri4soap.repo;

import net.mkaminski.sri4soap.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    List<Employee> findAll();
}
