package main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import main.model.Setting;


public interface SettingRepository extends JpaRepository<Setting, Long>{

}
