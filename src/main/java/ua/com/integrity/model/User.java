/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.integrity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private String name;
    private String nameEn;
    @JsonIgnore
    private Date birthDate;
    @JsonIgnore
    private String sex;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private Date hireDate;
    @JsonIgnore
    private String mobile;
    @JsonIgnore
    private String skype;
    private String email;
    @JsonIgnore
    private String position;
    @JsonIgnore
    private String department;
    @JsonIgnore
    private String departmentManager;
    @JsonIgnore
    private String unit;
    @JsonIgnore
    private String unitManager;
    @JsonIgnore
    private String company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentManager() {
        return departmentManager;
    }

    public void setDepartmentManager(String departmentManager) {
        this.departmentManager = departmentManager;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitManager() {
        return unitManager;
    }

    public void setUnitManager(String unitManager) {
        this.unitManager = unitManager;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return (name != null ? name.equals(user.name) : user.name == null) && (email != null ? email.equals(user.email) : user.email == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{"
                + "name='" + name + '\''
                + ", birthDate=" + birthDate
                + ", sex='" + sex + '\''
                + ", status='" + status + '\''
                + ", hireDate=" + hireDate
                + ", mobile='" + mobile + '\''
                + ", skype='" + skype + '\''
                + ", email='" + email + '\''
                + ", position='" + position + '\''
                + ", department='" + department + '\''
                + ", departmentManager='" + departmentManager + '\''
                + ", unit='" + unit + '\''
                + ", unitManager='" + unitManager + '\''
                + ", company='" + company + '\''
                + '}';
    }

}
