# Student Management Web App

---

## Assignment
* Create an Angular Web App for **Student Management**.
* **Admins** can **create, update, and remove** student records.
* **Staff** can only **view** student details.

* **Student Details:**

  * `regNo`
  * `rollNo`
  * `name`
  * `standard`
  * `school`

---

## How to Run

1. **Go to Root**

```bash
cd student-mgmt-app
```

2. **Install dependencies**:

```bash
npm install
```

3. **Run the Angular development server**:

```bash
ng serve
```

4. **Open in browser**:

```
http://localhost:4200
```

5. **Login**:

   * Use **Admin credentials** to add, update, or delete student records.
   * Use **Staff credentials** to view student details only.

---

✅ This setup uses **role-based access** (guards) to control functionality between Admin and Staff users.
