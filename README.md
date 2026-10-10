# STEP Semester 3

Daily progress log for Semester 3 coursework (Version Control & Daily Workflow Standard).

## Date: 10-10-2026

**Today's Work:**
- Added Week 9 Data Structures practice + assignment problems to `feature/session_9` under the `datastructures` package (branched from `develop`):
  - Practice in `class_problems`: P1_PairSumSorted (two pointers, O(n)/O(1) vs O(n^2) brute force), P2_WarehouseGridScan (single pass, strict-greater keeps first max, O(m*n)/O(1)), P3_LibraryCatalogLookup (hand-rolled binary search, O(log n)), P4_LibraryLookupBinarySearch (same duplicated PDF task via Arrays.binarySearch + Comparator), P5_MaxSumSubarrayK (sliding window O(n)/O(1) vs O(n*k) naive)
  - Assignments in `assigment_problems`: MallFootfallReport (prefix sums O(n+q)/O(n), [22, 31, 27, 9]), LongestBudgetStreak (variable window, non-negativity essential, (4, 4)/(0, -1)), NetBalancePeriodCounter (prefix-frequency map O(n)/O(n), 4/0), ExamScoreBandCounter (dual lower-bounds O(log n), 6/0), SpiralStockAuditRoute (four-boundary spiral O(m*n)/O(1))
- All ten compile-clean in per-file temp dirs and match every PDF sample output exactly.

**Next Session Plan:**
- Take up the next week's session problems (topic TBD) and log them here.

**Issues Faced:**
- None.

---

## Date: 10-10-2026

**Today's Work:**
- Added Week 7 Category A practice + assignment problems (abstract classes & interfaces) to the recreated `feature/session_7` under the `abstraction` package (branched from `develop`):
  - Practice in `class_problems`: P1_CheckoutPaymentHandler (abstract PaymentMethod, final TXN-1001/1002 ids, note-appending overload, upcasting), P2_HomeSafetyAlertNetwork (Alertable works with zero shared ancestry, DualZone super-reuse, instanceof downcast), P3_QuarterlyBonusCalculator (this()/super() chaining, negative-salary rejection, interface-based audit check), P4_UniversalMediaLauncher (IS-A AudioFile vs CAN-DO Podcast, play(int) overload, MF-1001), P5_CommunityLibraryCheckout (siblings with different capabilities, reserveIfSupported(Object))
  - Assignments in `assigment_problems`: DrawingCanvas (abstract Shape, SHP-1001 ids, uniform scale 4.0 to 64.0, circle ~78.54), DataExport (unrelated classes, one shared counter, exportAll + total 2), FleetMaintenanceTracker (3-deep hierarchy with super-reuse, Insurable as orthogonal interface), ArenaBattleSimulator (Warrior dual interfaces, standalone Trap, CHR-1001), HomeControlPanel (sibling capability split, standalone MobileApp, HD-1001 serials)
- All ten compile-clean in per-file temp dirs and match every PDF sample output (one real bug caught: Square scale(2.0) must grow the side once to 64.0, not twice).

**Next Session Plan:**
- Take up the next week's session problems (topic TBD) and log them here.

**Issues Faced:**
- None.

---

## Date: 10-10-2026

**Today's Work:**
- Added Week 8 Category A assignment problems to `feature/session_8` under the `oops_design` package in `assigment_problems` (same branch as the Week 8 class problems):
  - HackathonJudgingDesk (ScoringPolicy interface with Innovation 50/30/20 vs Open average, 2–4 member + one-team-per-student rules, OPEN → JUDGING → PUBLISHED lock, final 7.90, rescore rejected)
  - SwiftShipParcelTracker (BOOKED → … → DELIVERED transitions inside Parcel, Express 2 kg = ₹110.00, Fragile reuses Standard via composition, SMS+Email fan-out, cancel only while BOOKED, bad jump rejected)
  - SmartLabControlPanel (Device holds Capability objects — composition over inheritance, per-capability range guards, scene applies to capable devices only: 5 actions, 12°C rejected, runtime Brightness added to Projector)
  - ElectiveSeatRush (polymorphic credit limits Regular 24 / Honors 28 / Exchange 20 checked before seats, private FIFO waitlist, atomic drop-and-promote: Neha 16/20)
  - CanteenSmartCard (balance DERIVED from transaction ledger so balance = sum holds structurally, Hosteller 10% off: 108/54/360-vs-338, one-time refund, mini-statement +500/-108/-54/+108 = ₹446.00; stdout forced to UTF-8 so ₹/→/°C bytes are exact)
- All five compile-clean in per-file temp dirs and match every PDF sample output (byte-verified: no '?' replacements).

**Next Session Plan:**
- Take up the next week's session problems (topic TBD) and log them here.

**Issues Faced:**
- None.

---

## Date: 10-10-2026

**Today's Work:**
- Added CodInClub OOP design class problems (Week 8) to `feature/session_8` under the `oops_design` package in `class_problems` (branched from `develop`):
  - P1_OnlineExaminationSystem (abstract Question with MCQQuestion + TrueFalseQuestion, Attempt IN_PROGRESS -> SUBMITTED one-way state, single submitted attempt per student/exam, polymorphic evaluation 1/2 correct)
  - P2_VehicleRentalSystem (abstract Vehicle with Standard $50/day, Luxury $100/day, SUV $75/day; RentalService rent/charge/return, double-booking refused, Luxury 3 days $300.00 / Standard 5 days $250.00)
  - P3_HotelBookingSystem (STANDARD $150 / DELUXE $200 / SUITE $300 per night, overlap check rejects Deluxe 2024-12-03 to 2024-12-07, cancellation only before checkIn.minusDays(1); $800.00 / $600.00 totals)
  - P4_EmployeeLeaveManagement (FullTime/PartTime/Contract via abstract Employee, PENDING -> APPROVED/REJECTED guard inside LeaveRequest, illegal revert refused)
  - P5_FoodOrderPaymentSystem (Order-LineItem composition, non-empty invariant, IPaymentMethod with CreditCard success / DigitalWallet failure, order ids only consumed on placement so notifications read #123 and #124)
- All five compile-clean in per-file temp dirs and match every PDF sample output.

**Next Session Plan:**
- Take up the next week's session problems (topic TBD) and log them here.

**Issues Faced:**
- None.

---

## Date: 21-09-2026

**Today's Work:**
- Added Week 6 Category A class problems (Inheritance & Polymorphism practice, CineHub EventTicket family) to `feature/session_6` under the `polymorphism` package in `class_problems`:
  - P1_EventTicketFoundation (validated EventTicket ctor as only validation site, WorkshopTicket via super(...), registerBatch counting rejections with try/catch)
  - P2_TicketFamilyClassifier (multilevel PremiumWorkshopTicket + hierarchical HackathonTicket, instanceof-only classifyGeneration, polymorphic getTotalBalanceDue, printTicket overrides)
  - P3_LateFeeAuditTrail (@Override applyLateFee doubling via super.applyLateFee(amount * 2), private late-fee history with defensive copies)
  - P4_TicketAnnouncer (polymorphic batchPrint with a single StringBuilder, instanceof-guarded downcast to track)
  - P5_TicketIssuanceSettlement (final public ticketId from shared static counter "TCK-1001", charAt/isDigit/isUpperCase promo-code check, overloaded pay, null-safe processNightlySettlement)
- All five compile-clean in per-file temp dirs and match every PDF sample output.

**Next Session Plan:**
- Add dated log entries for each completed coding session above this one.

**Issues Faced:**
- None.

---

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