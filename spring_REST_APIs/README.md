# Student REST APIs - README

## 🔹 Sample PostgreSQL Data

```sql
INSERT INTO student (reg_no, roll_no, name, standard, school, gender, percentage) VALUES
(1, 101, 'Alice Johnson', 10, 'Sunrise High', 'FEMALE', 92.5),
(2, 102, 'Bob Smith', 10, 'Sunrise High', 'MALE', 88.0),
(3, 103, 'Charlie Brown', 9, 'Greenwood School', 'MALE', 29.5),
(4, 104, 'Diana Prince', 11, 'Sunrise High', 'FEMALE', 95.0),
(5, 105, 'Ethan Hunt', 12, 'Everest Academy', 'MALE', 85.5),
(6, 106, 'Fiona Gallagher', 9, 'Greenwood School', 'FEMALE', 81.0),
(7, 107, 'George Martin', 10, 'Everest Academy', 'MALE', 78.5),
(8, 108, 'Hannah Lee', 11, 'Sunrise High', 'FEMALE', 35.0),
(9, 109, 'Ian Somerhalder', 12, 'Everest Academy', 'MALE', 90.5),
(10, 110, 'Julia Roberts', 9, 'Greenwood School', 'FEMALE', 87.0);
````

---

# 🔹 API Endpoints (Example Queries & Bodies)


## 1️⃣ Get All Students (GET)

```
http://localhost:8080/students
```

---

## 2️⃣ Get Student By RegNo (GET)

```
http://localhost:8080/students/1
```

---

## 3️⃣ Create Student (POST)

```
http://localhost:8080/students
```

### Body (JSON)

```json
{
  "reg_no": 11,
  "roll_no": 111,
  "name": "Kevin Hart",
  "standard": 10,
  "school": "Sunrise High",
  "gender": "MALE",
  "percentage": 82.5
}
```

---

## 4️⃣ Update Student (PUT)

```
http://localhost:8080/students/11
```

### Body

```json
{ 
  "reg_no": 11,
  "roll_no": 120,
  "name": "Sia Mehta",
  "standard": 11,
  "school": "Everest Academy",
  "gender": "FEMALE",
  "percentage": 91.0
}
```

---

## 5️⃣ Partial Update (PATCH)

```
http://localhost:8080/students/11
```

### Body

```json
{
  "percentage": 38.0
}
```

---

## 6️⃣ Delete Student (DELETE)

```
http://localhost:8080/students/11
```

---

## 7️⃣ Get Students By School (GET)

```
http://localhost:8080/students/school?name=Sunrise High
```

---

## 8️⃣ Get Count By School (GET)

```
http://localhost:8080/students/school/count?name=Sunrise High
```

---

## 9️⃣ Get Count By Standard (GET)

```
http://localhost:8080/students/school/standard/count?standard=10
```

---

## 🔟 Get Strength (GET)

```
http://localhost:8080/students/strength?gender=MALE&standard=10
```

---

## Get Result (GET)

### ✅ Passed Students (≥ 40%)

```
http://localhost:8080/students/result?pass=true
```

### ❌ Failed Students (< 40%)

```
http://localhost:8080/students/result?pass=false
```

