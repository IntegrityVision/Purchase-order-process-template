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

package ua.com.integrity.model.techorder;

import ua.com.integrity.model.User;

public class TechOrder {

    private String id;
    private User owner;
    private String request;
    private String techToOrder;
    private Double techCost;
    private Boolean warehouse;
    private String comment;
    private User declineUser;
    private String declineReason;

    public String getId() {
        return id;
    }

    public User getDeclineUser() {
        return declineUser;
    }

    public void setDeclineUser(User declineUser) {
        this.declineUser = declineUser;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Boolean warehouse) {
        this.warehouse = warehouse;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    public String getTechToOrder() {
        return techToOrder;
    }

    public void setTechToOrder(String techToOrder) {
        this.techToOrder = techToOrder;
    }

    public Double getTechCost() {
        return techCost;
    }

    public void setTechCost(Double techCost) {
        this.techCost = techCost;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
