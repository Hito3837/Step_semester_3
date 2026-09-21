# STEP Semester 3

Daily progress log for Semester 3 coursework (Version Control & Daily Workflow Standard).

## Date: 21-09-2026

**Today's Work:**
- Added Week 6 Category A assignment problems (Inheritance & Polymorphism) to `feature/session_6` under the `polymorphism` package in `assigment_problems` (branched from `develop`):
  - RaceEntry.java (P1: validated base constructor as the only validation site, RunnerEntry via `super(...)`, registerBatch counting rejections with try/catch)
  - EliteRunnerEntry.java (P2: multilevel EliteRunnerEntry + hierarchical RelayTeamEntry, instanceof-only classifyGeneration, polymorphic getTotalBalanceDue, announce() overrides)
  - RunnerEntry.java (P3: `@Override` applyLateFee doubling via `super.applyLateFee(amount * 2)`, private late-fee history with defensive copies)
  - RaceAnnouncer.java (P4: polymorphic announceAll with a single StringBuilder, instanceof-guarded downcast to team size)
  - NightlySettlementEngine.java (P5: final entryCode from shared static counter, charAt/isDigit/isUpperCase discount-code check, overloaded pay, null-safe settleNight)
- All five compile-clean in per-file temp dirs and match every PDF sample output (bib counter reached 4 after 4 valid constructions; the rejected "B1" did not increment it).

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- None.

---

## Date: 21-09-2026

**Today's Work:**
- Added Week 5 live-session class problems (P1-P5) to `feature/session_5` under the `inheritance` package in `class_problems`:
  - P1_AccessRuleEngine (classifyAccess from Java visibility rules, summarizeBatch counter, PatientRecord with no no-arg constructor + patientId blank/whitespace/short rejection)
  - P2_CrossPackageReachLinter (extended classifyAccess for protected cross-package subclass OWN_TYPE vs PARENT_TYPE, describeContext title-case formatter)
  - P3_VitalsMonitoringGuard (PatientVitals all-private fields, silent range rejection via recordReading reused by the seeding constructor, defensive copies)
  - P4_PatientProfileJavaBean (three constructors chained with this(...), JavaBean getX/setX/isX, write-once setPatientId, write-only lockerPin)
  - P5_ImmutableDischargeSummary (MED-[A-Z] medication validation, final fields + defensive copies + wither, CriticalCareDischargeSummary extends, static-block shared state, instanceof batch settlement, null-safe)
- All five compile-clean in per-file temp dirs and match every PDF sample output.

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- PDF titled "Access Modifiers, Encapsulation & Object Modeling" (internal "Week 6" branding); filed under the Week 5 inheritance package per handoff. P5 spec said "class itself final" but also required CriticalCareDischargeSummary to extend it — kept final fields/defensive copies for immutability and made the leaf subclass the final type instead.

---

## Date: 21-09-2026

**Today's Work:**
- Added Week 4 live-session class problems (P1-P5) to `feature/session_4` under the `constructors` package in `class_problems`:
  - P1_BusTicketBookingValidator (BusTicket: parameterized constructor, meaningful-name validation, processBatch)
  - P2_FareSplitter (chained constructors, fair paisa-exact fare split, overdue confirmation validation)
  - P3_BusRouteRanking (this-clash resolution, constructor chaining with default priority, stable insertion sort ranking)
  - P4_BoardingPenaltyCalculator (final class/field/method, O(1) tiered penalty with 1% floor)
  - P5_NightlyFleetReconciliation (static block state, chained provisional constructor, Sleeper subclass with `instanceof` settlement)
- All five compile-clean and match the PDF sample outputs (verified per-file; P5 required a null-guard in processBatch after a first-run NPE).

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- P5 first ran into a NullPointerException: calling `accounts[i].processAccount(...)` on a null element before the guard; fixed by checking `accounts[i] == null` in the loop before invoking.

---

## Date: 21-09-2026

**Today's Work:**
- Added Week 3 live-session class problems (F1-F5: AttendanceSystem, FeeAccountInheritance, HostelAllocation, InstanceVsStatic, FeeHostelMiniSystem) to `feature/session_3` under the `class_object` package in `class_problems`.

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- F1/F4/F5 each declare their own `SrmStudent` and F2/F3/F5 re-declare `FeeAccount`/`HostelRoom`; files are standalone per problem and are not compiled together.

---

## Date: 21-09-2026

**Today's Work:**
- Added Session 5 assignment problems (AccessChecker, BookInventory, CirculationLedger, LibraryMember, LibraryMemberJavaBean, LoanReceipt) to `feature/session_5` under the `inheritance` package in `assigment_problems`.
- Marked with a placeholder `class_problems` package; to be filled with the live-session problems.

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- `LibraryMember.java` and `LibraryMemberJavaBean.java` both declare a class named `LibraryMember`, so the whole folder cannot be compiled in one `javac` run.

---

## Date: 21-09-2026

**Today's Work:**
- Added Session 4 assignment problems (Canteen, DeliveryAccount, DeliverySlot, FoodOrder, SurgeFeeCalculator) to `feature/session_4` under the `constructors` package in `assigment_problems`.
- Marked with a placeholder `class_problems` package; to be filled with the live-session problems.

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- None

---

## Date: 21-09-2026

**Today's Work:**
- Reorganized the repository into the STEP GitHub structure: README-only `main` branch, empty project skeleton on `develop`, and one feature branch per session with `class_problems` and `assigment_problems` packages.

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- None